import json
import ast
import re
import mysql.connector
from mysql.connector import errorcode
from datetime import datetime, date

base_dir = 'data/'
base_sql = 'sql/'
config = {
    'user': 'tripmate',
    'password': 'tripmate',
    'host': '127.0.0.1',
    'database': 'tripmate',
    'raise_on_warnings': True,
    'auth_plugin': 'mysql_native_password'
}
FLAG = True # rebuild database tables

def load_allsites(filename='all_sites.json'):
    with open(base_dir+filename) as f:
        all_sites = json.load(f)
    return all_sites

def load_entries(filename='entries.txt'):
    with open(base_dir+filename, 'r') as f:
        content = f.read().splitlines()
    entries = []
    i = 0
    while i < len(content)-1:
        entries.append((content[i], ast.literal_eval(content[i+1])))
        i += 2
    return entries

def load_pois(filename = 'pois_bc.txt'):
    with open(base_dir+filename, 'r') as f:
        content = f.read().splitlines()
    pois = [ast.literal_eval(line) for line in content]
    
    pois = [tuple((poi[0], poi[1], poi[2], parse_dms(poi[3]), parse_dms(poi[4]))) for poi in pois]
    return pois

def escape_single_quote(tup):
    return tuple([x.replace("'", "\"") for x in tup])

def replace_empty(string):
    return 'NULL' if len(string) == 0 else string

def get_entities(entries):
    states = [item[1][0] for item in entries]
    cities = [tuple(item[1][:-1]) for item in entries]
    pois = [(item[1][-1], item[1][-2].split(',')[0].strip(), item[0]) for item in entries]
    return states, cities, pois

def add_states2db(states, cursor, DEBUG=False):
    cprint('saving states into database')
    add_state = ("INSERT INTO state (name) ",
                 "SELECT '{}' ",
                 "WHERE NOT EXISTS ",
                 "(SELECT 1 FROM state WHERE name = '{}')")
    for state in states:
        data_state = (state,)
        data_state = (*data_state, data_state[0])
        data_state = escape_single_quote(data_state)
        command = ''.join(add_state).format(*data_state)
        if DEBUG: print('[EXECUTE]:\t', command)
        cursor.execute(command)
    return

def add_cities2db(cities, cursor, DEBUG=False):
    cprint('saving cities into database')
    add_city=  ("INSERT INTO city (name, state_id_fk)",
                "SELECT '{}',",
                "(SELECT id FROM state WHERE name = '{}') ",
                "WHERE NOT EXISTS ",
                "(SELECT 1 FROM city WHERE name = '{}');")
                
    for city_tuple in cities:
        if len(city_tuple) == 1:
            state, city_list = '', [city_tuple[0].strip()]
        else:
            city_list = [x.split(',')[0].strip() for x in city_tuple[1:]]
            state = city_tuple[0].strip()
        
        for city in city_list:
            data_city = (city, state)
            data_city = (*data_city, data_city[0])
            data_city = escape_single_quote(data_city)
            command = ''.join(add_city).format(*data_city)
            if DEBUG: print('[EXECUTE]:\t', command)
            cursor.execute(command)
    return

def add_pois2db(pois, cursor, DEBUG=False):
    cprint('saving pois into database')
    add_poi =  ("INSERT INTO poi (name, city_id_fk, wiki_url, last_updated, lat, lng) ",
                "SELECT '{}', '{}', '{}', '{}', {}, {} ",
                "WHERE NOT EXISTS ",
                "(SELECT 1 FROM poi WHERE name = '{}' AND city_id_fk = '{}');")
    for poi in pois: 
        city_name = poi[1]
        regex = "SELECT id FROM city WHERE name = '{}';"
        command = regex.format(city_name)
        cursor.execute(command)
        tmp = cursor.fetchone()
        if tmp:
            city_id_fk = str(tmp[0])
        else:
            city_id_fk = '0'
        new_poi = (poi[0], city_id_fk, poi[2], datetime.now().strftime("%s"), poi[3], poi[4], poi[0], city_id_fk)
        new_poi = escape_single_quote(new_poi)
        new_poi = tuple([replace_empty(x) for x in new_poi])
        command = ''.join(add_poi).format(*new_poi)
        if DEBUG: print('[EXECUTE]:\t', command)
        cursor.execute(command)
    return 

def drop_tables(cursor, filename = 'drop_tables.sql'):
    cprint('dropping tables')
    with open(base_sql+filename) as f:
        content = f.read().splitlines()
    commands = ''.join(content).split(';')[:-1]
    commands = [x+';' for x in commands]
    for command in commands:
        cursor.execute(command)

def rebuild_tables(cursor, filename = 'create_tables.sql'):
    cprint('rebuilding tables')
    with open(base_sql+filename) as f:
        content = f.read().splitlines()
    commands = ''.join(content).split(';')[:-1]
    commands = [x+';' for x in commands]
    for command in commands:
        cursor.execute(command)

def dms2dd(degrees, minutes, seconds, direction):
    dd = float(degrees) + float(minutes)/60 + float(seconds)/(60*60);
    if direction == 'S' or direction == 'W':
        dd *= -1
    return dd;

def dd2dms(deg):
    d = int(deg)
    md = abs(deg - d) * 60
    m = int(md)
    sd = (md - m) * 60
    return [d, m, sd]

def parse_dms(dms):
    if dms == None:
        return ''
    parts = re.split('[^\d\w]+', dms)
    if len(parts) == 4:
        lat = dms2dd(parts[0], parts[1], parts[2], parts[3])
    elif len(parts) == 3:
        lat = dms2dd(parts[0], parts[1], 0, parts[2])
    elif len(parts) == 5:
        lat = dms2dd(parts[0], parts[1], parts[2]+'.'+parts[3], parts[4])

    return str(lat)[:11]

def cprint(*message):
    print('[SAVE_TO_DATABASE] -- ', end='')
    print(*message)

if __name__== "__main__":

    entries = load_entries()
    states, cities, _ = get_entities(entries)
    pois = load_pois()
    try:
        cnx = mysql.connector.connect(**config)
        cursor = cnx.cursor()

        if FLAG:
            drop_tables(cursor)
            rebuild_tables(cursor)

        add_states2db(set(states), cursor)
        add_cities2db(set(cities), cursor)
        add_pois2db(set(pois), cursor)
        cnx.commit()
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print(err)
    finally:
        cnx.rollback()
        cnx.close()
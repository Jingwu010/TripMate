from bs4 import BeautifulSoup
import requests
import json

wiki_url = 'https://en.wikipedia.org/'
base_url = 'wiki/Category:Tourist_attractions_in_the_United_States_by_city'
base_dir = 'data/'

def read_keys(filename='keys.txt'):
    keys = list()
    with open(base_dir+filename) as f:
        for line in f:
            regexs.append(line.split('\n')[0])
    return keys

def get_url_pointers(soup):
    url_pointers = []
    for link in soup.find_all('a', class_='CategoryTreeLabelCategory'):
        href = link.get('href')
        title = link.get('title')
        if 'Tourist attractions' in title:
            title = title.replace('Category:Tourist attractions in ', '').replace(' by city', '')
            url_pointers.append((href, title))
    return url_pointers

def get_site_pointers(soup):
    site_pointers = []
    for link in soup.find_all('div', class_='mw-category-group'):
        for li in link.ul.children:
            if li.next_element:
                if li.next_element.get('href'):
                    site_pointers.append((li.next_element.get('href'), li.next_element.get('title')))
    return site_pointers

def get_children_sites(url, DEBUG=False):
    all_sites = dict()
    if DEBUG:
        print('get page', wiki_url+url)
    page = requests.get(wiki_url+url)
    soup = BeautifulSoup(page.content, 'html.parser')
    url_pointers = get_url_pointers(soup)
    site_pointers = get_site_pointers(soup)
    
    if DEBUG:
        print(len(url_pointers), 'url_pointers')
        print(len(site_pointers), 'site_pointers')
        
    for child_url in url_pointers:
        all_sites[child_url[1]] = get_children_sites(child_url[0], DEBUG)
    for child_site in site_pointers:
        all_sites[child_site[1]] = child_site[0]
        if DEBUG:
            print(child_site[1], child_site[0])
    return all_sites

def get_coordinates(url):
    page = requests.get(url)
    soup = BeautifulSoup(page.content, 'html.parser')
    if soup.find('span', class_='latitude'):
        return (soup.find('span', class_='latitude').get_text(), soup.find('span', class_='longitude').get_text())
    else:
        return ((None, None))

def unfold_dict(my_dict, path=[]):
    pairs = []
    for key, value in my_dict.items():
        cpath = path + [key]
        if type(value) == dict:
            pairs = pairs + unfold_dict(value, path=cpath)
        else:
            pairs.append((wiki_url+value, cpath))
    return pairs

def get_all_sites(filename='all_sites.json'):
    cprint('download from url :', wiki_url+base_url)
    all_sites = get_children_sites(base_url)
    cprint('write to json file :', base_dir+filename)
    json_data = json.dumps(all_sites)
    f = open(base_dir+filename,"w")
    f.write(json_data)
    f.close()
    return all_sites

def unfold_allsites(all_sites, filename='entries.txt'):
    cprint('unfold allsites dict into file :', base_dir+filename)
    entries = unfold_dict(all_sites)
    with open(base_dir+filename, 'w') as f:
        for item in entries:
            f.write("%s\n%s\n" % (item[0], item[1]))
    return entries

def cprint(*message):
    print('[DOWNLOAD_DATA] -- ', end='')
    print(*message)

if __name__== "__main__":
    all_sites = get_all_sites()
    entries = unfold_allsites(all_sites)
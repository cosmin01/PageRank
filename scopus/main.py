#!/usr/bin/env python

import csv

import unirest

SCOPUS_API_KEY = 'b3a71de2bde04544495881ed9d2f9c5b'  # '6dab753e3a22e58c28b719f039cc5f45'
list_of_requests = []


def remove_text_inside_brackets(text, brackets="()[]"):
    count = [0] * (len(brackets) // 2)  # count open/close brackets
    saved_chars = []
    for character in text:
        for i, b in enumerate(brackets):
            if character == b:  # found bracket
                kind, is_close = divmod(i, 2)
                count[kind] += (-1) ** is_close  # `+1`: open, `-1`: close
                if count[kind] < 0:  # unbalanced bracket
                    count[kind] = 0
                break
        else:  # character is not a bracket
            if not any(count):  # outside brackets
                saved_chars.append(character)
    return ''.join(saved_chars)


with open(r'F:\pROJECTS\scopus\files\CORE_2014.csv', 'rb') as csv_file:
    reader = csv.reader(csv_file, delimiter=',')
    for row in reader:
        conference_title = row[1]
        remove_text_inside_brackets(conference_title)
        queryString = 'conf(%s)' % conference_title
        url = 'https://api.elsevier.com:443/content/search/scopus?query=%s&apiKey=%s' % (queryString, SCOPUS_API_KEY)
        list_of_requests.append(url)

import json


def callback_function(response):
    if response.code != 200:
        print json.loads(response.raw_body)['service-error']['status']['statusText']
    else:
        print json.loads(response.raw_body)['search-results']['entry'][0]['dc:title']


unirest.timeout(25)
for url in list_of_requests:
    thread = unirest.get(url, headers={"Accept": "application/json"}, callback=callback_function)

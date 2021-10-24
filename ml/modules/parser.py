#!pip install --upgrade google-cloud-vision
#!pip install --upgrade google-cloud-translate
#!pip install --upgrade google-cloud-texttospeech

#!export LC_ALL="ru_RU.UTF-8"
#!export LC_CTYPE="ru_RU.UTF-8"
#!sudo dpkg-reconfigure locales

#import os
#os.environ["GOOGLE_APPLICATION_CREDENTIALS"]='axial-app-329720-3d06d3816130.json'

import html
import io
import os
import numpy as np

# Imports the Google Cloud client libraries
from google.api_core.exceptions import AlreadyExists
from google.cloud import texttospeech
from google.cloud import translate_v3beta1 as translate
from google.cloud import vision

import re

import datetime
import locale

import pandas as pd
import sys

from geopy.geocoders import Nominatim

#locale.setlocale(locale.LC_TIME, 'ru_RU.UTF-8')

class Parser:
    def __init__(self):
        self.geolocator = Nominatim()

    def pic_to_text(self, infile):
        """Detects text in an image file

        ARGS
        infile: path to image file

        RETURNS
        String of text detected in image
        """

        # Instantiates a client
        client = vision.ImageAnnotatorClient()

        # Opens the input image file
        with io.open(infile, "rb") as image_file:
            content = image_file.read()

        image = vision.Image(content=content)

        # For dense text, use document_text_detection
        # For less dense text, use text_detection
        response = client.document_text_detection(image=image, )
        text = response.full_text_annotation.text
        return text

    def get_id(self, text):
        # Strict versions - with '_' delimeter
        # zero match - search for PVN with 3 nums
        zero_match = re.search(r'[a-zA-z/\\0-9]+_\S+_\S+_\d+_\d+_\d+', text)
        if zero_match:
            return self.construct_id(text, zero_match.group(), 'PVN_hd_', 2)

        # first match - search for parks
        first_match = re.search(r'[a-zA-z/\\0-9]+_\S+_\d+_\d+_\d+', text)
        if first_match:
            return self.construct_id(text, first_match.group(), 'PARK_', 1)

        # second match - search for PVN
        second_match = re.search(r'[a-zA-z/\\0-9]+_\S+_\S+_\d+_\d+', text)
        if second_match:
            return self.construct_id(text, second_match.group(), 'PVN_hd_', 2)

        # third match - search for UVN
        third_match = re.search(r'[a-zA-z/\\0-9]+_\S+_\d+_\d+', text)
        if third_match:
            return self.construct_id(text, third_match.group(), 'UVN_', 1)

        # Light versions - with '_' or ' ' delimeters
        zero_match = re.search(r'[a-zA-z/\\0-9]+( |_)\S+( |_)\S+( |_)\d+( |_)\d+( |_)\d+', text)
        if zero_match:
            return self.construct_id(text, zero_match.group().replace(' ', '_'), 'PVN_hd_', 2)

        # first match - search for parks
        first_match = re.search(r'[a-zA-z/\\0-9]+( |_)\S+( |_)\d+( |_)\d+( |_)\d+', text)
        if first_match:
            return self.construct_id(text, first_match.group().replace(' ', '_'), 'PARK_', 1)

        # second match - search for PVN
        second_match = re.search(r'[a-zA-z/\\0-9]+( |_)\S+( |_)\S+( |_)\d+( |_)\d+', text)
        if second_match:
            return self.construct_id(text, second_match.group().replace(' ', '_'), 'PVN_hd_', 2)

        # third match - search for UVN
        third_match = re.search(r'[a-zA-z/\\0-9]+( |_)\S+( |_)\d+( |_)\d+', text)
        if third_match:
            return self.construct_id(text, third_match.group().replace(' ', '_'), 'UVN_', 1)
        return np.nan

    def construct_id(self, text, id, start, reg_cand_idx):
        ans = start
        reg_cand = id.split('_')[reg_cand_idx]
        reg = self.get_reg(text, reg_cand)
        if reg != np.nan:
            ans += reg + '_' + id.split(reg_cand + '_')[1]
            return ans
        return np.nan

    def get_reg(self, text, candidate):
        proper_names = {'TSAO': 'TSAO', 'TSA0': 'TSAO', 'SVAO': 'SVAO', 'UVAO': 'UVAO', 'SVA0': 'SVAO',
                        'UVA0': 'UVAO', 'UZAO': 'UZAO', 'UZA0': 'UZAO', 'SZAO': 'SZAO', 'SZA0': 'SZAO',
                        'SAO': 'SAO', 'VAO': 'VAO', 'UAO': 'UAO', 'ZAO': 'ZAO', 'SА0': 'SAO', 'VA0': 'VAO',
                        'UA0': 'UAO', 'ZA0': 'ZAO'}
        corrections = {'5A0': 'SAO', '8ZAO': 'SZAO', 'AO': 'VAO', 'BAO': 'SAO', 'IAO': 'ZAO', 'M': 'ZAO', 'MIAO': 'ZAO',
                       'SA0': 'SAO', 'SAC': 'SAO', 'SAD': 'SAO', 'SAN': 'SAO', 'SUA0': 'SVAO', 'SUR0': 'SVAO',
                       'SVA0': 'SVAO',
                       'SVAC': 'SVAO', 'SWA0': 'SVAO', 'SWAG': 'SWAO', 'SWAO': 'SVAO', 'SZA0': 'SZAO', 'T3A0': 'TSAO',
                       'T3AO': 'TSAO', 'TSA0': 'TSAO', 'U/A0': 'UVAO', 'UA0': 'UVAO', 'UNAO': 'UVAO', 'UVA0': 'UVAO',
                       'UVAC': 'UVAO', 'UWA0': 'UVAO', 'UWAO': 'UVAO', 'UWАО': 'UVAO', 'UZ0': 'UZAO', 'UZA': 'UZAO',
                       'UZA0': 'UZAO',
                       'UZAC': 'UZAO', 'UZAQ': 'UZAO', 'VAC': 'VAO', 'VAN': 'VAO', 'WA0': 'VAO', 'WAO': 'VAO',
                       'Z0': 'ZAO', 'ZA0': 'ZAO', 'ZAC': 'ZAO', 'SRO': 'SAO', 'SR0': 'SAO', 'gZAO': 'SZAO',
                       's/AO': 'SVAO', 's/A0': 'SVAO',
                       'sA0': 'SAO', 'sAO': 'SAO', 'sVAO': 'SVAO', 'sWAO': 'SVAO', 'sVA0': 'SVAO', 'sWA0': 'SVAO',
                       'sZA0': 'SZAO', 'sZAO': 'SZAO', 's\\/AO': 'SVAO', 'seo': 'SAO', 'svAO': 'SVAO', 'szA0': 'SZAO',
                       'szAO': 'SZAO',
                       'u/A0': 'UVAO', 'u/АО': 'UVAO', 'uWAO': 'UVAO', 'uwАО': 'UVAO', 'uzA0': 'UZAO', 'uzAO': 'UZAO',
                       'zA0': 'ZAO', 'zAO': 'ZAO',
                       'zko': 'ZAO', 'zАО': 'ZAO', 'АС': 'VAO'}

        # perfect match
        if candidate in proper_names:
            return proper_names[candidate]

        # search in whole text
        for name in proper_names.keys():
            if name in text:
                return proper_names[name]

        if candidate in corrections:
            return corrections[candidate]

        return np.nan

    def get_date(self, text):
        # most naive
        match = re.search(r'[0-9]{4}-[0-9]{2}-[0-9]{2}[ $&+,:;=?@#|<>.^*()%!-]{1}', text)
        if match:
            return match.group()[:10]

        # reversed
        match = re.search(r'[0-9]{2}-[0-9]{2}-[0-9]{4}[ $&+,:;=?@#|<>.^*()%!-]{1}', text)
        if match:
            return self.reverse(match.group()[:10])

        # looking for russian eq
        match = re.search(r'[(0-9)|(O)|(o)]{1}[0-9|(O)|(o)]{1} [А-я]+\.', text)
        if match:
            return self.convert_date_to_proper_format(match.group().replace('O', '0').replace('o', '0'))

        # may be something lost
        match = re.search(r'[0-9]{4}( |-|_)[0-9]{2}( |-|_)[0-9]{2}[ $&+,:;=?@#|<>.^*()%!-]{1}', text)
        if match:
            return match.group().replace(' ', '-').replace('_', '-')[:10]

        # reversed
        match = re.search(r'[0-9]{2}( |-|_)[0-9]{2}( |-|_)[ 0-9]{4}[$&+,:;=?@#|<>.^*()%!-]{1}', text)
        if match:
            return self.reverse(match.group().replace(' ', '-').replace('_', '-')[:10])

        return np.nan

    def convert_date_to_proper_format(self, russian_date):
        candidate = russian_date.lower()
        candidate = candidate.split(' ')[0] + ' ' + candidate.split(' ')[1][:3]
        result = datetime.datetime.strptime(candidate + ' 21', '%d %b %y')
        return result.strftime("%Y-%m-%d")

    def reverse(self, date):
        nums = date.split('-')
        return nums[2] + '-' + nums[1] + '-' + nums[0]

    def get_time(self, text):
        match = re.search(r' [0-9]{2}(:|\.| )[0-9]{2}(:|\.| )[0-9]{2}', text)
        if match:
            return match.group()[1:9].replace(' ', ':').replace('.', ':')

        return np.nan

    def prepare_address(self, initial_address, delimeter=','):
        address = ''
        parts = initial_address.split(delimeter)
        for part in parts:
            if 'город ' in part:
                address += part.split(' ')[-1]
                continue
            if 'бульвар' in part or 'улица' in part or 'проезд' in part or 'переулок' in part or 'набережная' in part or 'проспект' in part or 'шоссе' in part:
                address += ',' + part
                continue
            if 'дом ' in part:
                address += ', ' + part.split(' ')[-1]
            if 'корпус ' in part:
                address += ' к' + part.split(' ')[-1]
            if 'строение' in part:
                address += ' с' + part.split(' ')[-1]
        return address

    def address_to_coordinates(self, address):
        prepared_address = self.prepare_address(address)
        location = self.geolocator.geocode(prepared_address)
        if location:
            return location.latitude, location.longitude
        return np.nan, np.nan

    def get_info_from_photo(self, infile):
        """Detects id, date and time in an image file

        ARGS
        infile: path to image file

        RETURNS
        String of text detected in image - id, date, time
        NONE if nothing was found.
        """
        if not os.path.isfile(infile):
            raise RuntimeError('Provided path to picture is invalid: file does not exist')

        if not infile.lower().endswith(('.png', '.jpg', '.jpeg', '.tiff', '.bmp', '.gif')):
            raise RuntimeError('Provided file has incorrect extension: picture should be provided')

        text = self.pic_to_text(infile)

        return [self.get_id(text), self.get_date(text), self.get_time(text)]

    def get_info_from_photo_and_coordinates(self, infile, path_to_database):
        """Detects id, date and time in an image file and coordinates from database

        ARGS
        infile: path to image file

        RETURNS
        String of text detected in image - id, date, time
        NONE if nothing was found.
        """
        if not os.path.isfile(path_to_database):
            raise RuntimeError('Provided path to database is invalid: file does not exist')

        if not path_to_database.lower().endswith(('.xlsx')):
            raise RuntimeError('Provided file has incorrect extension: .xlsx format is the only option')

        # load database
        database = pd.read_excel(path_to_database)
        # get id
        id, date, time = self.get_info_from_photo(infile)
        # latitude and longitude
        address = ''
        if len(database[database['ID'] == id]) > 0:
            address = database[database['ID'] == id]['Address'].values[0]
        latitude, longitude = self.address_to_coordinates(address)
        return [id, date, time, address, latitude, longitude]
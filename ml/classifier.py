import os
import argparse
from PIL import Image
import numpy as np
import pandas as pd
from datetime import datetime
from functools import partial

from modules.select_instances import DetectObjects
from modules.parser import Parser
from modules.color import Color
from modules.tail import Tail
from modules.distance import distance

class Model():
    def __init__(self, db_path, det_config, det_check, color_model, tail_model):
        self.detector = DetectObjects(det_config, det_check)
        self.parser = Parser()
        self.color = Color(color_model)
        self.tail = Tail(tail_model)

        self.time_format = '%y-%m-%d %H:%M:%S'
        self.db = pd.read_csv(db_path)
        to_datetime = partial(datetime.strptime(), format=self.time_format)
        self.db.time = self.db.time.apply(to_datetime)

    def find(self, search_path, color, tail, address, time, search_radius=2.0):
        color = int(color)
        tail = int(tail)
        time = datetime.strptime(time, self.time_format)
        lat, lon = self.parser.address_to_coordinates(address)

        if color not in [0, 1, 2]:
            print(f'Invalid color {color}, expected to be in [0, 1, 2]')
            return None
        if tail not in [0, 1]:
            print(f'Invalid tail {tail}, expected to be in [0, 1]')

        db = self.db.dropna()
        filtered_time = db.drop(db[db.time < time].index)
        filtered_dist = filtered_time.drop(
            filtered_time[distance(filtered_time['lat'], filtered_time['lon'], lat, lon) > search_radius].index)
        filtered_tail = filtered_dist.drop(filtered_dist[filtered_dist['tail'] != tail].index)
        filtered_color = filtered_tail.drop(filtered_tail[filtered_tail['color'] != color].index)
        filtered = filtered_color.drop(filtered_color[filtered_color['is_the_owner_there']==1].index)

        filtered.to_csv(search_path)
        if len(filtered) > 0:
            print('Successfull search')
        else:
            print('Did not find any match')
        return filtered

    def add_to_db(self, img_path, path_to_addr_db):
        data = {'img_path': img_path}
        features = self.detector.detect_all_features(img_path)

        data['is_it_a_dog'] = features['is_dog_here']
        data['is_animal_there'] = features['is_animal_here']
        data['is_the_owner_there'] = features['is_dog_owner_here']
        uid, date, time, address, lat, lon = self.parser.get_info_from_photo_and_coordinates(img_path,
                                                                                             path_to_addr_db)
        data['time'] = ' '.join([date, time])
        data['uid'] = uid
        data['address'] = address
        data['lat'], data['lon'] = lat, lon

        data['bbox_path'] = np.nan
        if data['is_it_a_dog']:
            dog_detection = self.detector.detect_and_select_instances(img_path, 'dogs')
            if len(dog_detection[0][0]) != 0:
                img = Image.open(img_path)
                bbox = np.asarray(dog_detection[0][0][0][:4], dtype=np.int_)
                delta = np.zeros(2, dtype=np.int_)
                delta[0] = (bbox[2] - bbox[0]) // 10
                delta[1] = (bbox[3] - bbox[1]) // 10
                bbox[:2] = np.clip(bbox[:2] - delta, (0,0), img.size)
                bbox[2:] = np.clip(bbox[2:] + delta, (0,0), img.size)
                crop = img.crop(bbox)
                crop = crop.convert('RGB')
                crop.save(self.bbox_path(img_path))
                data['bbox_path'] = self.bbox_path(img_path)
                data['color'] = self.color.predict(data['bbox_path'])
                data['tail'] = self.tail.preditct(data['bbox_path'])
        series = pd.Series(data)
        self.db.append(series)
        print(f'{img_path} added successfully')

    @staticmethod
    def bbox_path(img_path):
        folder, img = os.path.split(img_path)
        subfolder, _ = img.split('.')
        cat, _ = os.path.split(folder)
        return os.path.join(cat, 'bbox', subfolder, img)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--color', type=int, default=0,
                        help='Input image color, 0 - multy colored, 1 - bright, 2 - dark')
    parser.add_argument('--tail', type=int, default=0,
                        help='Input image tail length, 0 - short, 1 - long')
    parser.add_argument('--address', type=str, help='Input image address')
    parser.add_argument('--time', type=str, default=datetime.now().strftime('%y-%m-%d %H:%M:%S'),
                        help='Time of losing the dog')
    parser.add_argument('--search_radius', type=float, default=2.0,
                        help='Search radius for cameras')
    parser.add_argument('--mode', type=str, default='search',
                        help='Search dog or add image to db: search, add')
    parser.add_argument('--img_path', type=str, default='', help='New img path')
    parser.add_argument('--search_path', type=str, default='', help='Results path for search')

    args = parser.parse_args()
    db_path = '/file-storage'
    det_config = '/ml/htc_x101_64x4d_fpn_16x1_20e_coco.py'
    det_check = '/ml/htc.pth'
    model = Model(db_path, det_config, det_check)
    if args.mode == 'search':
        model.find(args.search_path, args.color, args.tail, args.address, args.time, args.search_radius)
    elif args.mode == 'add':
        model.add_to_db(args.img_path)


if __name__ == '__main__':
    main()

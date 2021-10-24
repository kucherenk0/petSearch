#!/usr/bin/python3

import os
import cv2
import yaml
import mmdet
import numpy as np

from mmdet.apis import inference_detector, init_detector

from utils import (
    get_bbox_center,
    get_bbox_dims,
)


class DetectObjects(object):
    
    def __init__(
        self,
        config_path: str,
        checkpoint_path: str,
        id_category_coco: str='id_category_coco.yaml',
        relevant_classes: str='relevant_classes.yaml',
        device: str='cuda:0',
    ) -> None:
        
        '''
        Init of DetectObject class
        
        input: config_path(str) file with model pipeline 
            example: mmdetection/configs/htc/htc_x101_64x4d_fpn_16x1_20e_coco.py
        input: checkpoint_path(str) file with weights. Should be downloaded before init
            example: checkpoints/htc.pth
        input: id_category_coco(str) file path with class names for MC-COCO dataset
            example: id_category_coco.yaml
        input: relevant_classes(str) file path with common used classes for future selection 
        example: relevant_classes.yaml
        input: device(str) device (cuda or cpu)
            example: cuda:0
        
        output: None
        '''
        
        self.model = init_detector(
            config=config_path,
            checkpoint=checkpoint_path,
            device=device,
        )
        
        with open(id_category_coco, 'r') as file:
            idx_category = yaml.safe_load(file)
            
        self.id_category = {
            idx: idx_category[original_idx]
            for idx, original_idx in enumerate(idx_category)
        }
        
        self.category_id = {
            self.id_category[idx]: idx
            for idx in self.id_category
        }
        
        with open(relevant_classes, 'r') as file:
            relevant_classes = yaml.safe_load(file)
        
        self.select_classes = {
            base_class: {
                self.category_id[category]: category
                for category in relevant_classes[base_class]
            }
            for base_class in relevant_classes
        }
        
    def detect_and_select_instances_(
        self,
        predictions: tuple,
        taget_classes: str='dogs',
        treshold_confidence: float=1e-3,
    ) -> tuple:
        
        '''
        Selects target classes above treshold confidence from output mmdetection model
        
        input: predictions(tuple) output of mmdetection model after inference 
            example: inference_detector(model, path)
        input: taget_classes(str) write only those classes that are specified in self.select_classes.
            example: 'dogs' or 'humans' or 'animals'
        input: treshold_confidence(float) treshold confidence. All selected instances will be above the treshold
            example: 1e-2
            
        output: tuple(bbox, masks),
        bbox - list (N_CLASSES x N_OBJECTS x 5)
        masks - list (N_CLASSES x N_OBJECTS x HEIGHT x WIDTH)
        '''
        
        assert taget_classes in self.select_classes, \
            f'You wrote {taget_classes} in taget_classes. It supports only {self.select_classes.keys()}'
            
        bboxes, masks = predictions
        
        target_bboxes, target_masks = [], []

        for target_idx in self.select_classes[taget_classes]:
            filtered_bboxes, filtered_masks = [], []
            for bbox, mask in zip(bboxes[target_idx], masks[target_idx]):
                if len(bbox) == 0:
                    continue
                x_l, y_l, x_r, y_r, sc = bbox
                if sc > treshold_confidence:
                    filtered_bboxes.append(bbox)
                    filtered_masks.append(mask)
            target_bboxes.append(np.array(filtered_bboxes))
            target_masks.append(np.array(filtered_masks))

        target_result = (target_bboxes, target_masks)

        return target_result
    
    def who_are_here(
        self,
        predictions: tuple,
        taget_classes: str='dogs',
        treshold_confidence: float=1e-3,
    ) -> int:
        
        '''
        Are taget_classes on image?
        
        input: predictions(tuple) output of mmdetection model after inference 
            example: inference_detector(model, path)
        input: taget_classes(str) write only 'dogs' or 'animals'.
            example: 'dogs'
        input: treshold_confidence(float) treshold confidence. All selected instances will be above the treshold
            example: 1e-2
            
        output: int 1 if there are taget_classes on image, else 0
        '''
            
        assert taget_classes in self.select_classes, \
            f'You wrote {taget_classes} in taget_classes. It supports only {self.select_classes.keys()}'
            
        bboxes, masks = predictions

        for target_idx in self.select_classes[taget_classes]:
            for bbox in bboxes[target_idx]:
                if len(bbox) == 0:
                    continue
                x_l, y_l, x_r, y_r, sc = bbox
                if sc > treshold_confidence:
                    return True

        return False
    
    def is_dog_owner_here(
        self,
        predictions: tuple,
        treshold_confidence: float=1e-3,
    ) -> int:
        
        '''
        Is dog owner here on image?
        
        input: predictions(tuple) output of mmdetection model after inference 
            example: inference_detector(model, path)
        input: treshold_confidence(float) treshold confidence. All selected instances will be above the treshold
            example: 1e-2
            
        output: int 1 if there are dog owner on image, else 0
        '''
        
        humans_bbox, _ = self.detect_and_select_instances_(
            predictions=predictions,
            taget_classes='humans',
            treshold_confidence=treshold_confidence,
        )
        
        dogs_bbox, _ = self.detect_and_select_instances_(
            predictions=predictions,
            taget_classes='dogs',
            treshold_confidence=treshold_confidence,
        )
        
        if not self.who_are_here(predictions, 'dogs', treshold_confidence) or \
           not self.who_are_here(predictions, 'humans', treshold_confidence):
            
            return 0
        
        humans_bbox, _ = self.detect_and_select_instances_(
            predictions=predictions,
            taget_classes='humans',
            treshold_confidence=treshold_confidence,
        )

        dogs_bbox, _ = self.detect_and_select_instances_(
            predictions=predictions,
            taget_classes='dogs',
            treshold_confidence=treshold_confidence,
        )

        distances = []
        ignore_indexes = set()

        for dog_bbox in dogs_bbox[0]:

            temp_distances = []
            dog_center = get_bbox_center(dog_bbox)

            for human_bbox in humans_bbox[0]:

                human_center = get_bbox_center(human_bbox)

                temp_distances.append(
                    np.linalg.norm(human_center - dog_center)
                )

            index_match = np.array(temp_distances).argmin()

            if index_match not in ignore_indexes:
                ignore_indexes.add(index_match)
                human_dims = get_bbox_dims(humans_bbox[0][index_match])
                distances.append(
                    temp_distances[index_match] < 2*human_dims[1]
                )
                
        return int(any(distances))
            
    def detect_all_features(
        self,
        image_path: str,
        treshold_confidence: float=1e-3,
    ) -> dict:
        
        '''
        Detect all features on input image
        
        input: image_path(str) path of target image 
            example: image.jpg
        input: treshold_confidence(float) treshold confidence. All selected instances will be above the treshold
            example: 1e-2
            
        output: dict{
            'is_animal_here': int (0 - there is not, 1 there is),
            'is_dog_here': int (0 - there is not, 1 there is),
            'is_owner_here': int (0 - there is not, 1 there is),
        }
        '''
        
        assert os.path.exists(image_path), \
            f'Image path {image_path} does not exist'
        
        predictions = inference_detector(
            model=self.model,
            imgs=image_path,
        )
        
        request = {
            'is_animal_here': self.who_are_here(predictions, 'animals', treshold_confidence),
            'is_dog_here': self.who_are_here(predictions, 'dogs', treshold_confidence),
            'is_dog_owner_here': self.is_dog_owner_here(predictions, treshold_confidence),
        }
        return request
        
        
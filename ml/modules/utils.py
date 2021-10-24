#!/usr/bin/python3

import numpy as np

def get_bbox_center(bbox) -> tuple:
    
    xl, yl, xr, yr, _ = bbox
    
    xc = (xl + xr) / 2
    yc = (yl + yr) / 2
    
    return np.array([xc, yc])

def get_bbox_dims(bbox) -> tuple:
    
    xl, yl, xr, yr, _ = bbox
    
    w = xr - xl
    h = yr - yl
    
    return np.array([w, h])
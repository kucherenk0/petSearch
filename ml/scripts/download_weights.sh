#!/bin/bash

#download weights for nets

OUTPUT_PATH='/ml'
mkdir -p $OUTPUT_PATH

#for detection
DETECTION_NET='https://download.openmmlab.com/mmdetection/v2.0/htc/htc_x101_64x4d_fpn_16x1_20e_coco/htc_x101_64x4d_fpn_16x1_20e_coco_20200318-b181fd7a.pth'
wget -c $DETECTION_NET -O $OUTPUT_PATH/htc.pth

#for colors
COLOR_NET='1-AxaxApZ5chfOKV0Vr8arKCr-wT7PEeA'
gdown --id $COLOR_NET --fuzzy --output $OUTPUT_PATH/color.pth

#for tails
TAIL_NET='1-HLqsbdgDp9rOygC7K_hbwm3zPautLT7'
gdown --id $TAIL_NET --fuzzy --output $OUTPUT_PATH/tail.pth

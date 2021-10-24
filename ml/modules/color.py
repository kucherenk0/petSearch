import torch
import torch.nn as nn
from torchvision import transforms
from PIL import Image

class Color():
    def __init__(self, path):
        torch.hub._validate_not_a_forked_repo=lambda a,b,c: True
        self.model = torch.hub.load('pytorch/vision:v0.10.0', 'resnet50', pretrained=True)
        self.model.fc = nn.Linear(in_features=self.model.fc.in_features, out_features=3)
        self.model.load_state_dict(torch.load(path))
        self.model.eval()
        for param in self.model:
            param.requires_grad_(False)
            
        self.transform = transforms.Compose([
            transforms.Resize((224, 224), interpolation=transforms.InterpolationMode.BILINEAR),
            transforms.ToTensor(),
            transforms.Normalize((0.485, 0.456, 0.406), (0.229, 0.224, 0.225)),
        ])
        self.device = torch.device('cuda') if torch.cuda.is_available() else torch.device('cpu')
        self.model.to(self.device)
        
    def predict(self, img_path):
        img = Image.open(img_path)
        h, w = img.size
        img = self.transform(img).view(1, 3, h, w).to(self.device)
        preds = self.model(img)
        preds = torch.argmax(preds, dim=1)[0].item()
        return preds
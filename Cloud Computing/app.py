from flask import Flask, jsonify, request
from food_classification import get_image_classify
from werkzeug.utils import secure_filename
import os
import requests
import json

app = Flask(__name__)

def create_static_dir():
    static_dir_path = 'static/'
    if not os.path.exists(static_dir_path):
        os.makedirs(static_dir_path)
    return static_dir_path


@app.route("/predict", methods=["GET", "POST"])
def predict():
    response = {}
    response['status'] = 'valid'
    response['error'] = ''
    response['results'] = {}
    response['results']['energi_kalori'] = 0
    response['results']['protein'] = 0.0
    response['results']['lemak'] = 0.0
    response['results']['karbohidrat'] = 0.0
    response['results']['serat'] = 0.0

    if request.method == 'GET':
        response['status'] = 'method not allowed'
        response['error'] = 'you are not allowed to access this resource by this method'
        return jsonify(response), 405
    elif request.method == 'POST':
        img_file = request.files.get('image')

        if img_file:
            #### kode
            img_filename = secure_filename(img_file.filename)
            img_path = os.path.join(create_static_dir(), img_filename)
            img_file.save(img_path)

            img_classify = get_image_classify(img_path)

            if img_classify and img_classify != 'None':
                url_request = 'https://api.edamam.com/api/food-database/v2/parser?&ingr='+ img_classify +'&app_id=5572abb7&app_key=e4a24a1243c55cdbaf90fb05f862c31f'
                food_details = requests.get(url_request)
                food_details = food_details.json()
                nutrients = food_details['parsed'][0]['food']['nutrients']

                response['results']['classify_result'] = img_classify
                response['results']['energi_kalori'] = nutrients['ENERC_KCAL']
                response['results']['protein'] = nutrients['PROCNT']
                response['results']['lemak'] = nutrients['FAT']
                response['results']['karbohidrat'] = nutrients['CHOCDF']
                response['results']['serat'] = nutrients['FIBTG']
                
            else:
                response['results']['classify_result'] = img_classify

        else:
            response['status'] = 'request not valid'
            response['error'] = 'please attach an image file to your request'
            return jsonify(response), 400

    return jsonify(response)

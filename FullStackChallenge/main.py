from crypt import methods
from email import message
from flask import request
from flask import  Flask
from flask import send_from_directory
from flask import send_file
from flask import Blueprint
import json
import requests

MILES_TO_METERS = 1609

app=Flask(__name__)


@app.route('/')
def index():
    return send_file("BusinessSearch.html")




@app.route('/yelpParam', methods=['GET'])
def yelpParam():
    
    term = request.args.get('keyword', '')
    latitude = request.args.get('lat', '')
    longitude = request.args.get('lng', '')
    categoriesTemp = request.args.get('category', '')
    categories = "+".join(categoriesTemp.split(" "))
    radiusStr = request.args.get('distance', '')
    radius = int(radiusStr) * MILES_TO_METERS
    # print(term, latitude, longitude, categories, radius)

    yelpUrl = 'https://api.yelp.com/v3/businesses/search'
    yelpHeaders = {'Authorization': 'Bearer 6ykbycSckHcr_i0yGNee-AhL4TrTWIjQ1B8Qlazi4JXkUv8OwsTHC3w7WxItejyxx_Q_jrMrslmpcuFAxCBGsrTk00VPwv512n39F5uQdMwGTtXUlWyXf9SP5T8-Y3Yx'}
    yelpParams = {
        'term': term,
        'latitude': latitude,
        'longitude': longitude,
        'categories': categories,
        'radius': radius
    }
    yelpJson = requests.get(yelpUrl, headers = yelpHeaders, params = yelpParams).json()

    # json.loads(res)
    # print(yelpJson)
    
    return yelpJson

@app.route('/yelpDetails', methods=['GET'])
def yelpDetails():
    
    yelpId = request.args.get('yelpId', '')
    
    print(yelpId)

    yelpDetailUrl = ' https://api.yelp.com/v3/businesses/' + yelpId
    yelpDetailHeaders = {'Authorization': 'Bearer 6ykbycSckHcr_i0yGNee-AhL4TrTWIjQ1B8Qlazi4JXkUv8OwsTHC3w7WxItejyxx_Q_jrMrslmpcuFAxCBGsrTk00VPwv512n39F5uQdMwGTtXUlWyXf9SP5T8-Y3Yx'}
    # yelpDetailParams = {
    #     'id': yelpId,
    # }
    yelpDetailJson = requests.get(yelpDetailUrl, headers = yelpDetailHeaders).json()

    # json.loads(res)
    # print(yelpDetailJson)
    
    return yelpDetailJson


    
    
if __name__=="__main__":
    app.run(port=2022,host="127.0.0.1",debug=True)
    # port=80,host="127.0.0.1",debug=True

# def get_form_json(request):
#     # get json data: 
#     json_bytes = request.body
#     # transform bytes into str
#     json_str = json_bytes.decode()


#     req_data = json.loads(json_str)
#     print(req_data['a'])
#     print(req_data['b'])
#     return HttpResponse('OK')




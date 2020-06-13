from pyfcm import FCMNotification
import requests

def send_fcm(email):

    # get app token
    # url = "http://1.249.116.227:3001/api/find/appTokens"
    # data = {'phone': phonenum}
    # response = requests.post(url, headers={'Authorization': 'cc090ddf71d2866a77af6280'}, data=data)
    # size = len(response.json())
    # appToken = []
    # for i in response.json():
    #    appToken.append(i['appToken'])

    # delete this afer conntecing api
    appToken = []
    appToken.append('dlpcB6yN5QY:APA91bHB0Xae6ef1KSASD3fgEWz_lQjImKVJXo60g98X6AFR0qLsXmUS3QXTkaef7p6bqbZFIdZHk7LnvHK0GS2nVgUIqIYY6_3i8M3lPTufwFDUS0yZozgm3eG3NE86X6xevWq76Ngg')

    # firebase kdy
    push_service = FCMNotification(api_key="AAAAf-TKEmg:APA91bHvxgjQrCce-ffZMyhIFjM7cnkzw7TXcOq3kLMxNR0XEfLjiSokjUzjXfxc1g2IB3dpPJL74qwa3_4Hg0bzXiyipJiLouwvDd9s6dNgmr7syQ6GZ_waqxH7js9l3nknRgMyweNh")

    # fcm message
    message_title = "This is an emergency"
    message_body = "No face detected"
    image_url = "https://ccbebe.io/video/test.png"
    clickAction = "MainActivity"

    data_message = {
        "title": message_title,
        "content": message_body,
        "imageUrl": image_url,
        "clickAction": clickAction
    }

    result = push_service.notify_multiple_devices(registration_ids=appToken, data_message=data_message)
    print(result)

send_fcm('test@email.com')
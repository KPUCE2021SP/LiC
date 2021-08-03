import os
import sys
import urllib.request
import requests
import json
import googletrans
import time


class GoogleTrans:
    '''
        기술스택이 저장되어 있는 title과 description내용을 한글로 번역하여 
        제공해주기 위한 파일입니다.
    '''

    def __init__(self, file_name):
        '''
        번역하고자 하는 파일 이름을 받아옵니다.
        여기서는 stack_information.json입니다.
        '''
        self.file_name = file_name
        self.translator = googletrans.Translator()

    def open_file(self):
        '''
        파일을 json형태로 열고 
        title부분과 description부분만 번역을 해줍니다.
        '''
        with open(f"./{self.file_name}", "r") as f:
            json_data = json.load(f)

        tools = json_data["data"]["tools"]["edges"][0]
        tool_keys = list(tools.keys())

        for i in range(len(tools)):
            try:
                tools[tool_keys[i]]["title"] = self.translate(tools[tool_keys[i]]["title"])
                tools[tool_keys[i]]["description"] = self.translate(
                    tools[tool_keys[i]]["description"]
                )
            # except (TypeError, KeyError):
            #     pass
            except TypeError:
                i = i - 1
            except KeyError:
                pass
            except AttributeError:
                time.sleep(1)
        
        with open(f"./{self.file_name}", "w", encoding='utf-8') as make_file:
            json.dump(json_data, make_file, indent="\t", ensure_ascii=False)


    def translate(self, text):
        '''
        googletrans 라이브러리를 사용합니다.
        '''
        self.text = text
        self.result = self.translator.translate(self.text, dest="ko").text

        return self.result


if __name__ == "__main__":
    google = GoogleTrans("stack_information.json")
    google.open_file()

import os
import sys
import urllib.request
import json

from googletrans import Translator
import time
import parmap
import numpy as np
import multiprocessing



def translate_json(splited_data, tools):
    '''
    googletrans 라이브러리를 사용합니다.
    '''
    
    for data in splited_data:
        try:
            tools[data]["title"] = translator.translate(tools[data]["title"], dest="ko").text
            tools[data]["description"] = translator.translate(tools[data]["description"], dest="ko").text
        # except (TypeError, KeyError):
        #     pass
        except TypeError:
            pass
        except AttributeError:
            time.sleep(1)
        except:
            pass
        print(tools[data]["title"])
        print(tools[data]["description"])
            
        

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

    def open_file(self):
        '''
        파일을 json형태로 열고 
        title부분과 description부분만 번역을 해줍니다.
        '''
        num_cores = multiprocessing.cpu_count()
        with open(f"./{self.file_name}", "r") as f:
            self.json_data = json.load(f)

        self.tools = self.json_data["data"]["tools"]["edges"][0]
        self.tool_keys = list(self.tools.keys())

        # parmap 사용
        self.splited_data = np.array_split(self.tool_keys, num_cores)
        self.splited_data = [x.tolist() for x in self.splited_data]
        parmap.map(translate_json, self.splited_data, self.tools, pm_pbar=True, pm_processes=num_cores)

        with open("./stack_info_translate.json", "w", encoding='utf-8') as make_file:
            json.dump(json_data, make_file, indent="\t", ensure_ascii=False)

        


if __name__ == "__main__":
    translator = Translator()
    google = GoogleTrans("stack_information.json")
    google.open_file()
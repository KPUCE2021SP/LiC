import os
import sys
import urllib.request
import json


class Papago:
    """
    Papago 번역은 Papago의 인공 신경망 기반기계 번역 기술(NMT, Neural Machine Translation)로
    텍스트를 번역한 결과를 반환하는 RESTful API입니다.
    stack_information.json파일 부분에서 title과 description부분을 번역해준다.
    """

    def __init__(self, client_id, client_secret, file_name):
        """
        client_id, client_secret, api url 설정.
        file_name을 받음.
        """
        self.client_id = client_id
        self.client_secret = client_secret
        self.url = "https://openapi.naver.com/v1/papago/n2mt"
        self.file_name = file_name

    def translate_txt(self, source, target):
        """
        title부분과 description부분을 모두 수정해준 뒤
        기존 파일에 덮어 씌운다.
        """
        self.source = source
        self.target = target

        with open(f"./{self.file_name}", "r", encoding='UTF8') as f:
            json_data = json.load(f)

        info_dict = json_data[0]
        stack_data = list(info_dict)
        # amazon redshift 부터 돌리면 된다.
        for data in stack_data:
            try:
                info_dict[data]["title"] = self.translate(info_dict[data]["title"])
                info_dict[data]["description"] = self.translate(
                    info_dict[data]["description"]
                )
            except:
                pass

        with open(f"./{self.file_name}", "w", encoding="utf-8") as make_file:
            json.dump(json_data, make_file, indent="\t", ensure_ascii=False)

    def translate(self, text):
        """
        받은 text를 바탕으로 번역을 하는 함수이다.
        영어를 한글로 번역을 한 후 그 값을 리턴해준다.
        rescode error가 뜨면 rescode값을 출력해준다.
            N2MT01	400	source parameter is needed (source 파라미터가 필요합니다.)
            N2MT02	400	Unsupported source language (지원하지 않는 source 언어입니다.)
            N2MT03	400	target parameter is needed (target 파라미터가 필요합니다.)
            N2MT04	400	Unsupported target language (지원하지 않는 target 언어입니다.)
            N2MT05	400	source and target must be different (source와 target이 동일합니다.)
            N2MT06	400	There is no source-to-target translator (source->target 번역기가 없습니다.)
            N2MT07	400	text parameter is needed (text 파라미터가 필요합니다.)
            N2MT08	400	text parameter exceeds max length (text 파라미터가 최대 용량을 초과했습니다.)
            N2MT99	500	Internal server errors
        """
        encText = urllib.parse.quote(text)

        data = f"source={self.source}&target={self.target}&text=" + encText

        request = urllib.request.Request(self.url)
        request.add_header("X-Naver-Client-Id", self.client_id)
        request.add_header("X-Naver-Client-Secret", self.client_secret)
        response = urllib.request.urlopen(request, data=data.encode("utf-8"))

        rescode = response.getcode()
        if rescode == 200:
            response_body = json.loads(response.read().decode("utf-8"))
            result = response_body["message"]["result"]["translatedText"]
        else:
            print("Error Code:" + rescode)

        return result


if __name__ == "__main__":
    client_id = "JdN5vuGt14SUGF6itQaP"  # 발급받은 아이디 입력
    client_secret = "PTATH0GOol"  # 발급받은 secret입력
    file_name = "stack_information.json"

    papago = Papago(
        client_id=client_id, client_secret=client_secret, file_name=file_name
    )
    papago.translate_txt(source="en", target="ko")

    """
    ko : 한국어     fr : 프랑스어
    en : 영어       es : 스페인어
    ja : 일본어     it : 이탈리아어
    """

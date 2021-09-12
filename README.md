<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Thanks again! Now go create something AMAZING! :D
***
***
***
*** To avoid retyping too much info. Do a search and replace for the following:
*** github_username, repo_name, twitter_handle, email, project_title, project_description
-->
[![Code style: black](https://img.shields.io/badge/code%20style-black-000000.svg)](https://github.com/psf/black)
![Python: Version](https://img.shields.io/badge/python-3.9.5-blue)
![Docker: Version](https://img.shields.io/badge/docker-3.9-blue)


<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->

<!-- [![Forks][forks-shield]][forks-url] -->
<!-- [![Stargazers][stars-shield]][stars-url] -->
<!-- [![Contributors][contributors-shield]][contributors-url] -->
<!-- [![Issues][issues-shield]][issues-url] -->
<!-- [![MIT License][license-shield]][license-url] -->
<!--[![LinkedIn][linkedin-shield]][linkedin-url]-->



<!-- PROJECT LOGO -->
<br />
<p align="center">
  <!-- <a href="https://github.com/github_username/repo_name">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a> -->

  <h1 align="center">Stack Lounge</h1>

  <p align="center">
    국내 개발자들을 위한 기술 스택 정보 공유 서비스
    <br />
    <!-- <a href="https://github.com/github_username/repo_name"><strong>Explore the docs »</strong></a> -->
    <!-- <br />
    <br />
    <a href="https://github.com/github_username/repo_name">View Demo</a>
    ·
    <a href="https://github.com/github_username/repo_name/issues">Report Bug</a>
    ·
    <a href="https://github.com/github_username/repo_name/issues">Request Feature</a> -->
  </p>
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <!-- <li><a href="#installation">Installation</a></li> -->
      </ul>
    </li>
    <!-- <li><a href="#usage">Usage</a></li> -->
    <li><a href="#roadmap">Roadmap</a></li>
    <!-- <li><a href="#contributing">Contributing</a></li> -->
    <li><a href="#license">License</a></li>
    <!-- <li><a href="#contact">Contact</a></li> -->
    <li><a href="#team-members">Team</a></li>
    <li><a href="#wiki">Wiki</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

<!-- [![Product Name Screen Shot][product-screenshot]](https://example.com) -->
개발자가 되기 위해서는 언어만 다뤄서 되는 것이 아니죠. 다양한 기술 스택과 프레임워크를 알아야 현재 트렌드도 알 수 있고 뒤쳐지지 않을 수 있습니다. 해외에는 이미 비슷한 서비스가 있지만, 국내에는 기술 스택에 대해서 일반적으로 다가가기 쉽지 않은 상황입니다. 
</br>
**`Stack Lounge`는 이제 막 입문하기 시작하는 학생들과 새로운 배움을 추구하는 이들이 기술 스택에 조금 더 가까워질 수 있기 위한 서비스입니다.**
</br>
국내 IT 서비스 기업에서 사용하고 있는 기술 스택 정보를 기업별로 나열합니다.
필요한 기술 스택 정보는 `scrapy` 를 활용하며, 해당 기술 스택에 대한 선행사례는 기업들의 기술 블로그의 내용을 keyword extraction 을 통해 보여줍니다.

### Built With
<details>
<summary> <b> 이러한 기술들로 만들고 있습니다.</b></summary>
</br>

* ![Docker](https://img.shields.io/badge/-Docker-000000?style=flat&logo=docker)
* ![Django](https://img.shields.io/badge/-Django-000000?style=flat&logo=django)
* ![AndroidStudio](https://img.shields.io/badge/-AndroidStudio-000000?style=flat&logo=android)
* ![AWS EC2](https://img.shields.io/badge/-EC2-000000?style=flat&logo=amazon-aws)
* ![GraphQL](https://img.shields.io/badge/-GraphQL-000000?style=flat&logo=graphql)
* ![MongoDB](https://img.shields.io/badge/-MongoDB-000000?style=flat&logo=mongodb)
* ![Firebase](https://img.shields.io/badge/-Firebase-000000?style=flat&logo=firebase)
</details>
</br>

<!-- GETTING STARTED -->
## Getting Started

팀원들을 위한 프로젝트 기여 방법

<!-- ### Prerequisites

* pip
  ```sh
  npm install npm@latest -g
  ``` -->

### Prerequisites
기본적으로 전체 개발에 필요한 파이썬 패키지를 받아야 합니다.
1. 저장소를 클론 해주세요.
   ```sh
   git clone https://github.com/KPUCE2021SP/LiC.git
   ```
2. venv 를 생성합니다.
    ```sh
    # ubuntu 환경에서
    sudo apt-get update
    sudo apt-get install python3-venv
    python3 -m venv virtual-environment-name
    ```
3. 생성한 virtual environment 를 사용합니다.
    ```sh
    # ubuntu 환경에서
    source virtual-environment-name/bin/activate
    ```
4. 필요한 Python Library 를 pip 로 받습니다.
   ```sh
   # ubuntu 환경에서
   sudo apt-get install python3-pip
   cd LiC
   python3 -m pip install -r requirements.txt
   ```

### Starting Up
1. Docker Container를 시작해주세요
   ```sh
   docker-compose up -d
   ```
2. 컨테이너 확인 
    ```
    localhost:8081 -> MongoExpress
    localhost:8000/graphql -> Django / GraphQL
    ```

<!-- USAGE EXAMPLES -->
<!-- ## Usage

Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources.

_For more examples, please refer to the [Documentation](https://example.com)_
 -->


<!-- ROADMAP -->
## Roadmap

[open issues](https://github.com/KPUCE2021SP/LiC/issues)에 추가할 기능 사항을 올리고 개발할 때 참고해주세요.


## Basic Layout

![image](https://raw.githubusercontent.com/KPUCE2021SP/LiC/develop/.github/images/backbone.jpeg)
<!-- CONTRIBUTING -->
<!-- ## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request -->



<!-- LICENSE -->
## License

Distributed under the APACHE 2.0 License. See `LICENSE` for more information.



<!-- CONTACT -->
<!-- ## Contact

Your Name - [@twitter_handle](https://twitter.com/twitter_handle) - email

Project Link: [https://github.com/github_username/repo_name](https://github.com/github_username/repo_name) -->



<!-- ACKNOWLEDGEMENTS -->
## Team Members

* [홍성민](https://github.com/KKodiac)
* [배준일](https://github.com/bjo6300)
* [한상우](https://github.com/sktkddn777)


## Wiki
진행하면서 다양한 정리거리는 [위키참고](https://github.com/KPUCE2021SP/LiC/wiki)
[영어버젼](https://github.com/KPUCE2021SP/LiC/blob/develop/README_en.md)
<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo.svg?style=for-the-badge
[contributors-url]: https://github.com/github_username/repo/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo.svg?style=for-the-badge
[forks-url]: https://github.com/github_username/repo/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo.svg?style=for-the-badge
[stars-url]: https://github.com/github_username/repo/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo.svg?style=for-the-badge
[issues-url]: https://github.com/github_username/repo/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo.svg?style=for-the-badge
[license-url]: https://github.com/github_username/repo/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/github_username


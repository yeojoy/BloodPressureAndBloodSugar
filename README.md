# ForYou
아내를 위해 만든 프로젝트이다.

혈압과 혈당관리를 하기 위한 프로젝트로, 혈압(최대, 최소, 맥박)을 입력하고 혈당(혈당, 측정시각, 몸무게)를 입력하면  
화면에서 입력한 data를 보여준다.

Server를 따로 구성하지 않았으며 [parse.com](parse.com)을 사용해서 데이터를 저장하고 보여준다.

# How to run
현재 ApiKey라는 constants가 있는 interface가 .gitignore에 추가된 상태이다.  
그러므로
```
package me.yeojoy.foryou.config;

public interface ApiKey {
	String PARSE_APPLICATION_ID = "/* YOUR PARSE APP ID */";
    String PARSE_CLIENT_KEY		= "/* YOUR PARSE CLIENT KEY */"
}
```
추가해 줘야만 데이터를 저장하고 불러올 수 있다.

# TODO
- App icon 작업(디자이너 킴)
- 혈압, 혈당 icon 작업(디자이너 킴)
- 입력한 data 수정기능.
- graph를 line과 bar로 표현
- local db(sqlite3)과 sync 기능

# 3rd libraries
- Good FAB 3rd library > [Floating Action Button](https://github.com/Clans/FloatingActionButton)
- Amazing Graph library > [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
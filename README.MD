<center><img src="https://i.postimg.cc/MKPVVR1s/dplogo-512.png" alt="logo"></center>
<center><img src="https://i.postimg.cc/RZ9dqPFx/introduce.png" alt="introduce"></center>

이 플러그인은 마인크래프트 서버에서 플레이어들이 다양한 아이템을 수집하고, 목표를 달성하면 특별한 보상을 받을 수 있도록 도와줍니다. 복잡한 설정 없이, 쉽고 재미있게 아이템 수집 이벤트를 운영해보세요!

---

<center><img src="https://i.postimg.cc/RZ9dqP08/description.png" alt="description"></center>

- 다양한 아이템 수집 카테고리와 보상 만들기
- 플레이어별 수집 현황 자동 저장 및 확인
- GUI(메뉴)로 아이템과 보상 손쉽게 편집
- PlaceholderAPI 연동으로 수집 현황을 실시간으로 표시
- 관리자 명령어로 모든 기능을 간편하게 관리

---

<center><img src="https://i.postimg.cc/rwcjzhpH/depend-plugin.png" alt="depend-plugin"></center>

- 모든 DP-Plugin은 `DPP-Core` 플러그인이 필요합니다.
- 미설치 시 플러그인이 작동하지 않습니다.
- `DPP-Core` 플러그인은 <a href="https://github.com/DP-Plugins/DPP-Core/releases" target="_blank">Click me!</a>에서 다운로드할 수 있습니다.
- 이 플러그인은 `PlaceholderAPI` 플러그인과 연동됩니다.
- 미설치 시 해당 플러그인이 제공하는 플레이스 홀더 기능이 작동하지 않습니다.

---

<center><img src="https://i.postimg.cc/dV01RxJB/installation.png" alt="installation"></center>

1️⃣ `DPP-Core` 플러그인과 해당 플러그인 파일(`DP-ItemCollection-*.jar`)을 서버의 `plugins` 폴더에 넣어주세요.

2️⃣ 서버를 재시작하면 플러그인이 자동으로 활성화됩니다.

3️⃣ 필요하다면 `config.yml`과 `plugin.yml` 파일을 열어 원하는 설정을 바꿀 수 있어요.

---

<center><img src="https://i.postimg.cc/jSKcC85K/settings.png" alt="settings"></center>

- `config.yml`: 플러그인 기본 설정과 메시지 옵션을 관리합니다.

---

<center><img src="https://i.postimg.cc/SxqdjZKw/command.png" alt="command"></center>

❗ 일부 명령어는 관리자 권한(`dpic.admin`)이 필요합니다.

**명령어 목록 및 사용 예시**

| 명령어                                                    | 권한         | 설명                       | 예시                                                           |
|--------------------------------------------------------|------------|--------------------------|--------------------------------------------------------------|
| `/dpic create <collection/reward> <name>`              | dpic.admin | 새로운 아이템 수집 카테고리 또는 보상 생성 | `/dpic create collection 보물찾기`<br>`/dpic create reward 최종보상` |
| `/dpic maxpage <collection> <maxPage>`                 | dpic.admin | 카테고리의 최대 페이지 수 설정        | `/dpic maxpage 보물찾기 5`                                       |
| `/dpic items <collection/reward> <name>`               | dpic.admin | 카테고리/보상 아이템을 GUI로 편집     | `/dpic items collection 보물찾기`<br>`/dpic items reward 최종보상`   |
| `/dpic addcmdreward <rewardName> <command>`            | dpic.admin | 보상에 명령어 추가               | `/dpic addcmdreward 최종보상 say 축하합니다!`                         |
| `/dpic removecmdreward <rewardName> <index>`           | dpic.admin | 보상에 등록된 명령어 삭제           | `/dpic removecmdreward 최종보상 1`                               |
| `/dpic setreward <collectionName> <rewardName> <step>` | dpic.admin | 카테고리에 보상 연결              | `/dpic setreward 보물찾기 최종보상 3`                                |
| `/dpic removereward <collectionName> <step>`           | dpic.admin | 카테고리의 보상 연결 해제           | `/dpic removereward 보물찾기 3`                                  |
| `/dpic settotalreward <rewardName> <step>`             | dpic.admin | 전체 수집 보상 연결              | `/dpic settotalreward 최종보상 10`                               |
| `/dpic removetotalreward <step>`                       | dpic.admin | 전체 수집 보상 해제              | `/dpic removetotalreward 10`                                 |
| `/dpic checkitem`                                      | dpic.admin | 수집 체크 아이템 설정 GUI         | `/dpic checkitem`                                            |
| `/dpic open <collectionName>`                          | 없음         | 카테고리 수집 현황 GUI 열기        | `/dpic open 보물찾기`                                            |
| `/dpic openrewardclaim (collectionName)`               | 없음         | 보상 수령 GUI 열기             | `/dpic openrewardclaim`<br>`/dpic openrewardclaim 보물찾기`      |

**❗명령어 사용 시 주의사항**

- 카테고리명/보상명은 한글, 영어 모두 사용 가능하지만, 띄어쓰기는 지원하지 않습니다.
- 잘못된 명령어 입력 시 오류 메시지가 출력됩니다. 예시: "올바른 타입이 아닙니다. (collection/reward)"
- 아이템/보상 편집은 GUI를 통해 직관적으로 가능합니다. 변경 후 자동 저장됩니다.
- 관리자 명령어는 반드시 `OP` 또는 `dpic.admin` 권한이 필요합니다.

---

<center><img src="https://i.postimg.cc/Z5ZH0fqL/api-integration.png" alt="api-integration"></center>

서버 내에서 플레이어의 수집 현황을 실시간으로 보여줄 수 있어요!

- `%dpic_total_collected%`: 내가 수집한 아이템 개수
- `%dpic_total_collections%`: 전체 수집 가능한 아이템 개수

---

<center><a href="https://discord.gg/JnMCqkn2FX"><img src="https://i.postimg.cc/4xZPn8dC/discord.png" alt="discord"></a></center>

- 궁금한 점이나 문제가 있다면 서버 관리자에게 문의하거나, 플러그인 배포 페이지의 안내를 참고해 주세요.
- 더 좋은 기능이나 개선 아이디어가 있다면 언제든 의견을 남겨주세요!

---
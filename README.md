# 키친포스

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

---

## 도메인 해석

관리자는 상품(`product`)을 입력한다.
입력한 상품에 대해 메뉴(`menu`)를 만들고
상품은 메뉴의 기준이 된다.
상품메뉴(`menu_product`)를 통해 몇 개의 상품이 재고로 등록되어있는지 알 수 있다.
(한 메뉴에 대해 양념치킨이 두 마리 있는 개념은 아닌 것으로 보인다. 이유는 주문 항목(`order_line_item`)으로 따로 있다.)
메뉴 그룹(`menu_gruop`)은 메뉴들 묶은 것.

금액은 단가가 아니라 단가 * 수량이다.

주문 항목(`order_line_item`)은 한 메뉴에 대해 몇 개의 수량이 있는 지에 대한 것이다. (예: 양념치킨 두 마리)
주문(`orders`)은 매장에서 발생하는 주문이다.
(예: 양념치킨 두 마리와 간장 한 마리)
또 주문에는 상태가 있는데,
- 조리(COOKING), 식사(MEAL), 계산 완료(COMPLETION) 가 있다.
주문 테이블(`order_table`)은 같이 온 손님들에 대한 도메인이다. 따라서 손님 수(`number of guests`)라는 항목을 가지고 있다.
테이블 그룹은 주문 테이블들을 묶은 것입니다. (따라서 2개 이상이어야 의미가 있음)
(예: 테이블 단체 계산)



## 요구 사항

### MenuGroup
- 메뉴 그룹을 생성한다.
- 메뉴 그룹 리스트를 조회한다.


### Menu
- 메뉴를 생성한다.
  - `예외`
    - 메뉴 가격은 0 이상의 정수여야 한다.
    - 메뉴 그룹에 반드시 속해야 한다.
    - 존재하는 상품에 대해서는 메뉴로 등록이 가능하다.
    - 메뉴의 가격이 (상품 가격 * 양)들의 총합보다 클 수 없다. (`#q`)
      - `price.compareTo(sum) > 0`
- 메뉴 리스트를 조회한다.


### Product
- 상품을 생성한다.
  - `예외`
    - 상품 가격은 0 이상의 정수여 한다.
- 상품 리스트를 조회한다.


### Order
- 주문을 생성한다.
  - `정상 흐름`
    - 주문의 상태를 조리(Cooking)로 변경한다.
    - 주문일을 현재로 갱신한다.
  - `예외`
    - 빈 주문을 생성할 수 없다.
    - 주문 결재선 개수가 메뉴의 개수와 다를 수는 없다. (`#q`)
      - `orderLineItems.size() != menuDao.countByIdIn(menuIds)`
  - 주문에 해당하는 주문 테이블이 없을 수는 없다. 
- 주문 리스트를 조회한다.
- 주문 상태를 변경한다.


### Table
- 테이블을 생성한다.
  - `정상 흐름`
    - 초기 ID는 null 로 세팅한다.
    - 초기 그룹 ID는 null로 세팅한다.
- 테이블 리스트를 조회한다.
- 테이블 비움 상태를 변경한다.
  - `예외`
    - 실제 주문 테이블이 존재해야 한다.
    - 그룹 ID는 비어있을 수 없다.
    - 계산 완료(`COMPLETION`)된 테이블에 대해서만 상태를 변경할 수 있다.
- 테이블의 손님 숫자를 변경한다.
  - `예외`
    - 손님 수를 0 미만으로 변경할 수 없다.
    - 존재하지 않는 주문 테이블을 요청할 수 없다.
    - 기존 주문 테이블이 비어있는 경우 손님 수를 변경할 수 없다.


### TableGroup
- 테이블 그룹을 생성한다.
  - `정상 흐름`
    - 그룹의 생성일을 오늘로 갱신한다.
  - `예외`
    - 그룹의 크기는 2 이상이어야 합니다.
    - 저장되어 있던 테이블의 크기와 생성할 테이블의 수는 다를 수 없다.
- 테이블 그룹을 해제한다.
  - `정상 흐름`
    - 테이블의 ID를 null로 초기화한다.
    - 테이블의 상태를 비어있지 않음으로 초기화한다.
  - `예외`
    - 계산 완료(`COMPLETION`)된 테이블에 대해서만 상태를 변경할 수 있다.


### 첨자
- `(#q)` 는 직관적으로 이해하기 난해한 부분을 마킹.

# ADR002. Domain Coupling
- createdDate: 2025-10-23
- updatedDate: 2025-10-24

## Context
### 우리 서비스에서 현재 발생하는 도메인 결합이 어떤게 있는가?
#### Order, Dispatch 간 결합
- 기본 설명
  - Order: 유저의 요청
  - Dispatch: 유저의 요청을 배송하기 위한 차량 배차
  - 우리 서비스 역사적으로 가장 유명한 결합이다.
  - 과거에는 dispatch 내 orderId 를 두어 결합이 발생했다. 현재에는 mapping 테이블을 두고 서로를 매핑하고있어 이전보다는 느슨해졌다.

### 도메인 결합을 점진적으로 줄여나가기 위해서는 어떤 방식을 채택해야 하는가?
- 결합
  - 상태 변경이 해당한다. order와 dispatch는 서로의 상태에 관심이 있다. dispatch가 confirmed로 변경되면, order는 ready로 변경된다.
  - 해당 상태를 컨트롤하는 API는 여러 개로 분산되어있다.
  - 애초부터 이 둘의 상태 연관관계가 맺어진 것이 문제였을까? 점진적으로 나아가기 위해서는 어떤 방식이 필요한가?
    - 사실 현재 서비스에서도 상태의 연관은 맺어져있지 않다.
    - 다만 service layer에서 두 상태를 관리하는 주체가 있다.
    - Service layer에서는 domain layer로 요청만 보내야 하지, 값을 직접 컨트롤하면 안 된다.
    - 이는 내용 결합으로 표현될 수 있다.
  - 둘은 상/하 관계일까? 동등한 관계일까?
    - 지금은 동등한 관계라고 표현할 수 있을 것 같다.

## Decision
- 상태 변경 로직은 domain model에 응집한다.
  - Servive layer에서 값을 직접 입력하는 것이 아니라, domain model 메소드에 요청하도록 한다.
  - 상태 변경 관련 비즈니스 규칙은 domain model만 확인하면 된다.
- 분산된 상태 변경 관련 service 및 api를 응집한다.
  - 처음부터 합치지 않고, api endpoint를 만들어서 client가 모두 해당 endpoint를 호출할 수 있도록 유도한다.

## Consequences
- 유저가 상태 변경을 못 할 수도 있다. 기존 API를 사용하는 client에서 변경된 API를 더이상 사용하지 못할 수 있다. 놓치지 않도록 한다.
- 각 api, service 별 상태 변경 로직이 다를 수 있다. 이는 잘 파악해서 먼저 도메인 로직에 응집할 수 있도록 한다.
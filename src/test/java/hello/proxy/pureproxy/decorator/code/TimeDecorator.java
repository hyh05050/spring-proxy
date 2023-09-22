package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeDecorator implements Component {

    private Component component;

    public TimeDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
      log.info("TIMEDECO 실행");
      long startTime = System.currentTimeMillis();
      String result = component.operation();
      long endTime = System.currentTimeMillis();
      long resultTime = endTime - startTime;
      log.info("TIMEDECO 종료 및 걸린시간:{}ms",resultTime);
      return result;
    }
}

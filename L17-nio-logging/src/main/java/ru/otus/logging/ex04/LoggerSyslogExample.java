package ru.otus.logging.ex04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sergey
 * created on 14.08.18.
 */


/*
yum install rsyslog

vi /etc/rsyslog.conf
ModLoad imudp
UDPServerRun 514

systemctl enable rsyslog
systemctl start rsyslog

sudo tail -f /var/log/messages
*/

public class LoggerSyslogExample {
  private static final Logger logger = LoggerFactory.getLogger(LoggerSyslogExample.class);
  private long counter = 0;

  public static void main(String[] args) throws InterruptedException {
    new LoggerSyslogExample().loop();
  }

  private void loop() throws InterruptedException {
    while (!Thread.currentThread().isInterrupted()) {
      logger.error("msg for Syslog:{}", counter);
      counter++;
      Thread.sleep(3_000);
    }
  }
}

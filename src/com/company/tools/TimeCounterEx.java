package com.company.tools;

import com.company.elements.GameArea;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTime;
import com.trolltech.qt.core.QTimer;


public class TimeCounterEx extends QObject
{
    private GameArea gameArea;

    private QTimer timer;
    private QTime time;
    private Boolean enableTimeCounting;


    public TimeCounterEx(GameArea gameArea)
    {
        this.gameArea = gameArea;
        this.enableTimeCounting = gameArea.getEnableTimeCounting();
    }

    public void countTime()
    {
        if ( enableTimeCounting )
        {
            timer = new QTimer(this);
            timer.timeout.connect(this, "updateTimeCounter()");
            timer.start(1000);
            time = new QTime(0, 0, 0);
            gameArea.setTimer(timer);
            gameArea.setTime(time);
        }
        else
        {
            gameArea.getMainWindow().updateTime("DISABLED");
        }
    }

    public void updateTimeCounter()
    {
        time = time.addSecs(1);
        String currentTime = time.toString("hh:mm:ss");
        gameArea.getMainWindow().updateTime(currentTime);
        gameArea.setTime(time);
    }
}

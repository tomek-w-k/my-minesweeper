package com.company.tools;

import com.company.MainWidget;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTime;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.QWidget;

public class TimeCounter extends QObject
{
    private MainWidget mainWidget;

    private QTimer timer;
    private QTime time;
    private Boolean enableTimeCounting;

    public TimeCounter(QWidget targetWidget)
    {
        this.mainWidget = (MainWidget)targetWidget;

        this.enableTimeCounting = mainWidget.enableTimeCounting;
    }

    public void timeCounter()
    {
        if ( timer != null )
        {
            timer.dispose();
        }
        if ( time != null )
        {
            time.dispose();
        }

        if ( enableTimeCounting )
        {
            timer  = new QTimer(this);
            mainWidget.timer = timer;
            timer.timeout.connect(this, "updateTimeCounter()");
            timer.start(1000);
            time = new QTime(0, 0, 0);
        }
        else
        {
            mainWidget.mw.updateTime("DISABLED");
        }
    }

    public void updateTimeCounter()
    {
        time = time.addSecs(1);
        String currentTime = time.toString("hh:mm:ss");
        mainWidget.mw.updateTime(currentTime);
        mainWidget.time = time;
    }
}

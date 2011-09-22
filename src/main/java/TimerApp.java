import eu.hansolo.steelseries.extras.Clock;
import org.joda.time.LocalTime;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.pushingpixels.trident.callback.TimelineCallback;
/**
 * User: chgv
 * Date: 9/21/11
 * Time: 9:33 AM
 */
public class TimerApp implements ActionListener {

    //components
    private JPanel mainPanel;
    private JLabel timerLabel;
    private JButton startButton;
    private JButton stopButton;
    private JSpinner hours;
    private JSpinner minutes;
    private JSpinner seconds;
    private JPanel timePanel;
    private Clock clock;

    //variables
    private Timer timer;
    private LocalTime localTime;
    private boolean isPause = false;

    //constants
    private static final int DEFAULT_STEP = 1000;
    private static final String TIME_PATTERN = DEFAULT_STEP >= 1000 ? "HH:mm:ss" : "HH:mm:ss.S";
    private static final String START_MESSAGE = "Hello!";
    private static final String APP_NAME = "TimerApp";
    private static final String START = "Start";
    private static final String PAUSE = "Pause";
    private static final String FINISH = "Finish";

    //entry point
    public static void main(String[] args) {
        JFrame frame = new JFrame(APP_NAME);
        frame.setContentPane(new TimerApp().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    //timer fired event
    public void actionPerformed(ActionEvent e) {
        if (localTime.getMillisOfDay() != 0) {
            localTime = localTime.minusMillis(DEFAULT_STEP);
            updateTimeLabel();
        } else {
            stopTimer();
        }
    }
    //init app
    public TimerApp() {
        timer = new Timer(DEFAULT_STEP, this);
        timerLabel.setText(START_MESSAGE);
        stopButton.setVisible(false);
        clock.setHour(0);
        clock.setMinute(0);
        clock.setSecond(0);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (localTime == null) {
                    localTime = new LocalTime(
                            (Integer) hours.getValue(),
                            (Integer) minutes.getValue(),
                            (Integer) seconds.getValue());
                    updateTimeLabel();
                    timePanel.setVisible(false);
                    stopButton.setVisible(true);
                }
                if (isPause) {
                    timer.stop();
                    startButton.setText(START);
                } else {
                    timer.start();
                    startButton.setText(PAUSE);
                }
                isPause = !isPause;
            }
        });
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopTimer();
            }
        });
    }

    private void stopTimer() {
        timer.stop();
        timerLabel.setText(FINISH);
        startButton.setText(START);
        isPause = false;
        localTime = null;
        timePanel.setVisible(true);
        stopButton.setVisible(false);
    }

    private void updateTimeLabel() {
        clock.setHour(localTime.getHourOfDay());
        clock.setMinute(localTime.getMinuteOfHour());
        clock.setSecond(localTime.getSecondOfMinute());
        timerLabel.setText(localTime.toString(TIME_PATTERN));
    }

    private void createUIComponents() {
        final SpinnerModel hourLimits = new SpinnerNumberModel(0, 0, 23, 1);
        hours = new JSpinner(hourLimits);
        final SpinnerModel minuteLimits = new SpinnerNumberModel(0, 0, 59, 1);
        minutes = new JSpinner(minuteLimits);
        final SpinnerModel secondsLimits = new SpinnerNumberModel(10, 0, 59, 10);
        seconds = new JSpinner(secondsLimits);
    }
}
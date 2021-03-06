package moe.haruue.goodhabits.ui.calendar;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import moe.haruue.goodhabits.config.Const;
import moe.haruue.goodhabits.data.FirstTime;
import moe.haruue.goodhabits.data.database.task.func.TasksByTimeQueryFunc;
import moe.haruue.goodhabits.model.Task;
import moe.haruue.goodhabits.util.TimeUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by simonla on 2016/8/18.
 * Have a good day.
 */
public class CalendarPresneter implements CalendarContract.Presenter {

    private CalendarContract.View mView;

    public CalendarPresneter(CalendarContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getFinishedPer(CalendarContract.Callback callback) {
        Observable.just(Task.newEmptyTaskWithStartTimeAndEndTime(TimeUtils.timeStampToDayStart(FirstTime.getFirstTimeLoginTimeStamp()),
                                                                 TimeUtils.getTimeStampOf(new GregorianCalendar())))
                .map(new TasksByTimeQueryFunc())
                .map(tasks -> {
                    int finishedTastCount = 0;
                    for (Task t: tasks) {
                        if (t.isFinish) {
                            finishedTastCount++;
                        }
                    }
                    return (int) (((double) finishedTastCount) / ((double) tasks.size()) * 100);
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("CalendarPresneter", "getFinishPer", e);
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        callback.onFinish(integer);
                    }
                });
    }

    @Override
    public void getFinishOfMonth(CalendarContract.FinishDayCallback callback) {
        Observable.just(new GregorianCalendar())
                .map(nowTime -> {
                    List<GregorianCalendar> dayInMonth = new ArrayList<>(0);
                    GregorianCalendar days = (GregorianCalendar) nowTime.clone();
                    TimeUtils.resetDayTimeToStart(days);
                    days.set(Calendar.DAY_OF_MONTH, 1);
                    do {
                        dayInMonth.add((GregorianCalendar) days.clone());
                        days.add(Calendar.DAY_OF_MONTH, 1);
                    } while (days.get(Calendar.DAY_OF_MONTH) <= nowTime.get(Calendar.DAY_OF_MONTH));
                    return dayInMonth;
                })
                .subscribeOn(Schedulers.computation())
                .map(daysInMonth -> {
                    HashMap<Integer, Boolean> finishMap = new HashMap<>(0);
                    for (GregorianCalendar d: daysInMonth) {
                        long startTimeStamp = TimeUtils.getTimeStampOf(d);
                        long endTimeStamp = startTimeStamp + 86400;
                        List<Task> tasksInDay = new TasksByTimeQueryFunc().call(Task.newEmptyTaskWithStartTimeAndEndTime(startTimeStamp, endTimeStamp));
                        boolean isFinishDay = true;
                        if (tasksInDay.isEmpty() || TimeUtils.getTimeStampOf(d) < TimeUtils.timeStampToDayStart(FirstTime.getFirstTimeLoginTimeStamp())) {
                            isFinishDay = false;
                        } else {
                            for (Task t : tasksInDay) {
                                if (!t.isFinish) {
                                    isFinishDay = false;
                                    break;
                                }
                            }
                        }
                        finishMap.put(d.get(Calendar.DAY_OF_MONTH), isFinishDay);
                    }
                    return finishMap;
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HashMap<Integer, Boolean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("CalendarPresenter", "getFinishOfMonth", e);
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(HashMap<Integer, Boolean> integerBooleanHashMap) {
                        callback.onFinish(integerBooleanHashMap);
                    }
                });
    }

    @Override
    public void getSkipPer(CalendarContract.Callback callback) {
        Observable.just(Task.newEmptyTaskWithStartTimeAndEndTime(TimeUtils.timeStampToDayStart(FirstTime.getFirstTimeLoginTimeStamp()),
                                                                 TimeUtils.getTimeStampOf(new GregorianCalendar())))
                .map(new TasksByTimeQueryFunc())
                .map(new Func1<List<Task>, Integer>() {
                    @Override
                    public Integer call(List<Task> tasks) {
                        int courseCount = 0, skipedCourseCount = 0;
                        for (Task t: tasks) {
                            if (Const.TASK_TYPE_SCHOOL_COURSE.equals(t.type)) {
                                courseCount++;
                                if (!t.isFinish) {
                                    skipedCourseCount++;
                                }
                            }
                        }
                        if (courseCount == 0) {
                            return 0;
                        } else {
                            return (int) ((double) skipedCourseCount / (double) courseCount * 100);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        callback.onFinish(integer);
                    }
                });
    }

    @Override
    public void getSkipMoreThanOthers(CalendarContract.Callback callback) {
        // TODO: Complete it with LeanCloud
        callback.onFinish(32);
    }
}

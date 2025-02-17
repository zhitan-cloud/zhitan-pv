package com.ruoyi.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计算的工具类
 *
 * @author Silence
 * @date 2022/3/8 15:57
 **/
public class CalcUtil {

    /**
     * 最大余额法，用于解决百分比不足100%或者超过100%的问题
     *
     * @param arr       数组
     * @param idx       索引
     * @param precision 精度
     * @return 每一次计算的结果
     */
    public static double getPercentValue(int[] arr, int idx, int precision) {
        if ((arr.length - 1) < idx) {
            return 0;
        }
        //求和
        double sum = 0;
        for (int j : arr) {
            sum += j;
        }
        if (sum == 0) {
            return 0;
        }
        //10的2次幂是100，用于计算精度。
        double digits = Math.pow(10, precision);
        //扩大比例100
        double[] votesPerQuota = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            double val = arr[i] / sum * digits * 100;
            votesPerQuota[i] = val;
        }
        //总数,扩大比例意味的总数要扩大
        double targetSeats = digits * 100;
        //再向下取值，组成数组
        double[] seats = new double[arr.length];
        for (int i = 0; i < votesPerQuota.length; i++) {
            seats[i] = Math.floor(votesPerQuota[i]);
        }
        //再新计算合计，用于判断与总数量是否相同,相同则占比会100%
        double currentSum = 0;
        for (double seat : seats) {
            currentSum += seat;
        }
        //余数部分的数组:原先数组减去向下取值的数组,得到余数部分的数组
        double[] remainder = new double[arr.length];
        for (int i = 0; i < seats.length; i++) {
            remainder[i] = votesPerQuota[i] - seats[i];
        }
        while (currentSum < targetSeats) {
            double max = 0;
            int maxId = 0;
            for (int i = 0; i < remainder.length; ++i) {
                if (remainder[i] > max) {
                    max = remainder[i];
                    maxId = i;
                }
            }
            //对最大项余额加1
            ++seats[maxId];
            //已经增加最大余数加1,则下次判断就可以不需要再判断这个余额数。
            remainder[maxId] = 0;
            //总的也要加1,为了判断是否总数是否相同,跳出循环。
            ++currentSum;
        }
        // 这时候的seats就会总数占比会100%
        return seats[idx] / digits;
    }

    /**
     * 最大余额法，用于解决百分比不足100%或者超过100%的问题
     *
     * @param arr       数组
     * @param idx       索引
     * @param precision 精度
     * @return 每一次计算的结果
     */
    public static double getPercentValue(BigDecimal[] arr, int idx, int precision) {
        if ((arr.length - 1) < idx) {
            return 0;
        }
        //求和
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal j : arr) {
            sum = sum.add(j);
        }
        if (sum.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        //10的2次幂是100，用于计算精度。
        double digits = Math.pow(10, precision);
        //扩大比例100
        double[] votesPerQuota = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
//            double val = arr[i].divide(sum) * digits * 100;
            double val = arr[i].divide(sum, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(digits * 100)).doubleValue();
            votesPerQuota[i] = val;
        }
        //总数,扩大比例意味的总数要扩大
        double targetSeats = digits * 100;
        //再向下取值，组成数组
        double[] seats = new double[arr.length];
        for (int i = 0; i < votesPerQuota.length; i++) {
            seats[i] = Math.floor(votesPerQuota[i]);
        }
        //再新计算合计，用于判断与总数量是否相同,相同则占比会100%
        double currentSum = 0;
        for (double seat : seats) {
            currentSum += seat;
        }
        //余数部分的数组:原先数组减去向下取值的数组,得到余数部分的数组
        double[] remainder = new double[arr.length];
        for (int i = 0; i < seats.length; i++) {
            remainder[i] = votesPerQuota[i] - seats[i];
        }
        while (currentSum < targetSeats) {
            double max = 0;
            int maxId = 0;
            for (int i = 0; i < remainder.length; ++i) {
                if (remainder[i] > max) {
                    max = remainder[i];
                    maxId = i;
                }
            }
            //对最大项余额加1
            ++seats[maxId];
            //已经增加最大余数加1,则下次判断就可以不需要再判断这个余额数。
            remainder[maxId] = 0;
            //总的也要加1,为了判断是否总数是否相同,跳出循环。
            ++currentSum;
        }
        // 这时候的seats就会总数占比会100%
        return seats[idx] / digits;
    }

    /**
     * 最大余额法，用于解决百分比不足100%或者超过100%的问题
     *
     * @param arr       数组
     * @param precision 精度
     * @return 按照数组顺序排列的百分比
     */
    public static double[] getPercentValue(int[] arr, int precision) {
        double[] result = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = getPercentValue(arr, i, precision);
        }
        return result;
    }

    /**
     * 最大余额法，用于解决百分比不足100%或者超过100%的问题
     *
     * @param arr       数组
     * @param precision 精度
     * @return 按照数组顺序排列的百分比
     */
    public static double[] getPercentValue(BigDecimal[] arr, int precision) {
        double[] result = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = getPercentValue(arr, i, precision);
        }
        return result;
    }
}

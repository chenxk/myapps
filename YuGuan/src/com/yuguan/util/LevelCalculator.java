package com.yuguan.util;

public class LevelCalculator {
	public static int calSkillLevel(int score) {
		if (score < 30)
			return 1;
		if (score < 70)
			return 2;
		if (score < 120)
			return 3;
		if (score < 180)
			return 4;
		if (score < 250)
			return 5;
		if (score < 330)
			return 6;
		if (score < 420)
			return 7;
		if (score < 520)
			return 8;
		if (score < 630)
			return 9;
		if (score < 750)
			return 10;
		if (score < 880)
			return 11;
		if (score < 1020)
			return 12;
		if (score < 1270)
			return 13;
		if (score < 1430)
			return 14;
		if (score < 1690)
			return 15;
		if (score < 1860)
			return 16;
		if (score < 2040)
			return 17;
		if (score < 2230)
			return 18;
		if (score < 2430)
			return 19;
		if (score < 2640)
			return 20;
		if (score < 2860)
			return 21;
		if (score < 3090) {
			return 22;
		}
		return 23;
	}

	public static int calRepatationStar(int score) {
		if (score < 88)
			return 1;
		if (score < 220)
			return 2;
		if (score < 440)
			return 3;
		if (score < 770)
			return 4;
		if (score < 1200)
			return 5;
		if (score < 1700)
			return 10;
		if (score < 3500)
			return 20;
		if (score < 6000)
			return 30;
		if (score < 9000)
			return 40;
		if (score < 13000)
			return 50;
		if (score < 18000)
			return 100;
		if (score < 24000)
			return 200;
		if (score < 31000)
			return 300;
		if (score < 40000) {
			return 400;
		}
		return 500;
	}
}

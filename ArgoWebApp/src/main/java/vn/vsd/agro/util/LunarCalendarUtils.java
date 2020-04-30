package vn.vsd.agro.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LunarCalendarUtils {
	public static double LOCAL_TIMEZONE = 7.0; // Local time zone counted as hours ahead of GMT

	public static final double PI = Math.PI;
	public static final double[] SUNLONG_MAJOR = new double[] {
		0, PI/6, 2*PI/6, 3*PI/6, 4*PI/6, 5*PI/6, PI, 7*PI/6, 8*PI/6, 9*PI/6, 10*PI/6, 11*PI/6
	};
	public static final String[] CAN = {"Giap", "At", "Binh", "Dinh", "Mau", "Ky", "Canh", "Tan", "Nham", "Quy"};
	public static final String[] CHI = {"Ty'", "Suu", "Dan", "Mao", "Thin", "Ty.", "Ngo", "Mui", "Than", "Dau", "Tuat", "Hoi"};

	static Map<Integer, int[][]> yearCache = new HashMap<Integer, int[][]>();

	/**
	 * Neu leap = 0: Can-Chi cua ngay dd thang mm nam yy am lich;
	 * Neu leap = 1: Can-Chi cua ngay dd thang mm nhuan nam yy am lich
	 * @param dd
	 * @param mm
	 * @param yy
	 * @param leap
	 * @return
	 */
	public static String CanChi(int dd, int mm, int yy, int leap) {
		int[] solar = Lunar2Solar(dd, mm, yy, leap);
		double jd = UniversalToJD(solar[0], solar[1], solar[2]);
		String canNgay = CAN[MOD(INT(jd+9.5), 10) % 10];
		String chiNgay = CHI[MOD(INT(jd+1.5), 12) % 12];
		String canThang = CAN[MOD((yy*12+mm+3), 10) % 10];
		String chiThang = CHI[MOD((mm+1), 12) % 12];
		String nhuan = leap == 1 ? " (nhuan)" : "";
		String canNam = CAN[MOD(yy+6, 10) % 10];
		String chiNam = CHI[MOD(yy+8, 12) % 12];
		
		return "Ngay " + canNgay + " " + chiNgay 
				+ ", thang " + canThang + " " + chiThang + nhuan 
				+ ", nam " + canNam + " " + chiNam;
	}

	public static int[][] getLunarYear(int yy) {
		int[][] ret = yearCache.get(new Integer(yy));
		if (ret == null) {
			ret = LunarYear(yy);
			if (yearCache.size() > 10) {
				yearCache.clear();
			}
			
			yearCache.put(new Integer(yy), ret);
		}
		
		return ret;
	}

	static void initLeapYear(int[][] ret) {
		double[] sunLongitudes = new double[ret.length];
		for (int i = 0; i < ret.length; i++) {
			int[] a = ret[i];
			double jdAtMonthBegin = LocalToJD(a[0], a[1], a[2]);
			sunLongitudes[i] = SunLongitude(jdAtMonthBegin);
		}
		boolean found = false;
		for (int i = 0; i < ret.length; i++) {
			if (found) {
				ret[i][3] = MOD(i+10, 12);
				continue;
			}
			double sl1 = sunLongitudes[i];
			double sl2 = sunLongitudes[i+1];
			boolean hasMajorTerm = false;
			for (int j = 0; j < SUNLONG_MAJOR.length; j++) {
				double sl3 = SUNLONG_MAJOR[j];
				hasMajorTerm = hasMajorTerm || (sl1 <= sl3 && sl3 < sl2 || sl1 > sl2 && sl1 <= sl3 || sl1 > sl2 && sl3 < sl2);
			}
			if (!hasMajorTerm) {
				found = true;
				ret[i][4] = 1;
				ret[i][3] = MOD(i+10, 12);
			}
		}
	}

	public static int INT(double d) {
		return (int)Math.floor(d);
	}

	public static int[] LocalFromJD(double JD) {
		return UniversalFromJD(JD + LOCAL_TIMEZONE/24.0);
	}

	public static double LocalToJD(int D, int M, int Y) {
		return UniversalToJD(D, M, Y) - LOCAL_TIMEZONE/24.0;
	}

	public static int[] Lunar2Solar(int D, int M, int Y, int leap) {
		int yy = Y;
		if (M >= 11) {
			yy = Y+1;
		}
		int[][] lunarYear = getLunarYear(yy);
		int[] lunarMonth = null;
		for (int i = 0; i < lunarYear.length; i++) {
			int[] lm = lunarYear[i];
			if (lm[3] == M && lm[4] == leap) {
				lunarMonth = lm;
				break;
			}
		}
		if (lunarMonth != null) {
			double jd = LocalToJD(lunarMonth[0], lunarMonth[1], lunarMonth[2]);
			return LocalFromJD(jd + D - 1);
		} else {
			throw new RuntimeException("Incorrect input!");
		}
	}

	public static int[] LunarMonth11(int Y) {
		double off = LocalToJD(31, 12, Y) - 2415021.076998695;
		int k = INT(off / 29.530588853);
		double jd = NewMoon(k);
		int[] ret = LocalFromJD(jd);
		double sunLong = SunLongitude(LocalToJD(ret[0], ret[1], ret[2])); // sun longitude at local midnight
		if (sunLong > 3*PI/2) {
			jd = NewMoon(k-1);
		}
		return LocalFromJD(jd);
	}

	public static int[][] LunarYear(int Y) {
		int[] month11A = LunarMonth11(Y-1);
		double jdMonth11A = LocalToJD(month11A[0], month11A[1], month11A[2]);
		int k = (int)Math.floor(0.5 + (jdMonth11A - 2415021.076998695) / 29.530588853);
		int[] month11B = LunarMonth11(Y);
		double off = LocalToJD(month11B[0], month11B[1], month11B[2]) - jdMonth11A;
		boolean leap = off > 365.0;
		
		int[][] ret = null;
		if (!leap) {
			ret = new int[13][5];
		} else {
			ret = new int[14][5];
		}
		ret[0] = new int[]{month11A[0], month11A[1], month11A[2], 0, 0};
		ret[ret.length - 1] = new int[]{month11B[0], month11B[1], month11B[2], 0, 0};
		
		for (int i = 1; i < ret.length - 1; i++) {
			double nm = NewMoon(k+i);
			int[] a = LocalFromJD(nm);
			ret[i] = new int[]{a[0], a[1], a[2], 0, 0};
		}
		
		for (int i = 0; i < ret.length; i++) {
			ret[i][3] = MOD(i + 11, 12);
		}
		
		if (leap) {
			initLeapYear(ret);
		}
		
		return ret;
	}

	public static void main(String[] args) {
		int y = 2004;
		for (int i = 1; i <= 12; i++) {
			int[] am, duong;
			am = Solar2Lunar(1, i, y);
			String nhuan = am[3] == 1 ? " nhuan" : "";
			String canChi = CanChi(am[0], am[1], am[2], am[3]);
			System.out.print("DL -> AL: 1/"+i+"/"+y+" = "+am[0]+"/"+am[1]+"/"+am[2]+nhuan+" ~ "+canChi);
			duong = Lunar2Solar(am[0], am[1], am[2], am[3]);
			System.out.println("\t\tAL -> DL: "+am[0]+"/"+am[1]+"/"+am[2]+nhuan+" = "+duong[0]+"/"+duong[1]+"/"+duong[2]);
		}
	}

	public static int MOD(int x, int y) {
		int z = x - (int)(y * Math.floor(((double)x / y)));
		if (z == 0) {
		  z = y;
		}
		return z;
	}
	
	/**
	 * Julian day number of the kth new moon after (or before) the New Moon of 1900-01-01 13:51 GMT.
	 * Accuracy: 2 minutes
	 * Algorithm from: Astronomical Algorithms, by Jean Meeus, 1998
	 * @param k
	 * @return
	 */

	public static double NewMoon(int k) {
		double T = k/1236.85; // Time in Julian centuries from 1900 January 0.5
		double T2 = T * T;
		double T3 = T2 * T;
		double dr = PI/180;
		double Jd1 = 2415020.75933 + 29.53058868*k + 0.0001178*T2 - 0.000000155*T3;
		Jd1 = Jd1 + 0.00033*Math.sin((166.56 + 132.87*T - 0.009173*T2)*dr); // Mean new moon
		double M = 359.2242 + 29.10535608*k - 0.0000333*T2 - 0.00000347*T3; // Sun's mean anomaly
		double Mpr = 306.0253 + 385.81691806*k + 0.0107306*T2 + 0.00001236*T3; // Moon's mean anomaly
		double F = 21.2964 + 390.67050646*k - 0.0016528*T2 - 0.00000239*T3; // Moon's argument of latitude
		double C1=(0.1734 - 0.000393*T)*Math.sin(M*dr) + 0.0021*Math.sin(2*dr*M);
		C1 = C1 - 0.4068*Math.sin(Mpr*dr) + 0.0161*Math.sin(dr*2*Mpr);
		C1 = C1 - 0.0004*Math.sin(dr*3*Mpr);
		C1 = C1 + 0.0104*Math.sin(dr*2*F) - 0.0051*Math.sin(dr*(M+Mpr));
		C1 = C1 - 0.0074*Math.sin(dr*(M-Mpr)) + 0.0004*Math.sin(dr*(2*F+M));
		C1 = C1 - 0.0004*Math.sin(dr*(2*F-M)) - 0.0006*Math.sin(dr*(2*F+Mpr));
		C1 = C1 + 0.0010*Math.sin(dr*(2*F-Mpr)) + 0.0005*Math.sin(dr*(2*Mpr+M));
		
		double deltat;
		if (T < -11) {
			deltat= 0.001 + 0.000839*T + 0.0002261*T2 - 0.00000845*T3 - 0.000000081*T*T3;
		} else {
			deltat= -0.000278 + 0.000265*T + 0.000262*T2;
		};
		
		return Jd1 + C1 - deltat;
	}

	public static int[] Solar2Lunar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		int D = calendar.get(Calendar.DATE);
		int M = calendar.get(Calendar.MONTH) + 1;
		int Y = calendar.get(Calendar.YEAR);
		
		return Solar2Lunar(D, M, Y);
	}
	
	public static int[] Solar2Lunar(int D, int M, int Y) {
		int yy = Y;
		int[][] ly = getLunarYear(Y);
		int[] month11 = ly[ly.length - 1];
		double jdToday = LocalToJD(D, M, Y);
		double jdMonth11 = LocalToJD(month11[0], month11[1], month11[2]);
		if (jdToday >= jdMonth11) {
			ly = getLunarYear(Y+1);
			yy = Y + 1;
		}
		int i = ly.length - 1;
		while (jdToday < LocalToJD(ly[i][0], ly[i][1], ly[i][2])) {
			i--;
		}
		int dd = (int)(jdToday - LocalToJD(ly[i][0], ly[i][1], ly[i][2])) + 1;
		int mm = ly[i][3];
		if (mm >= 11) {
			yy--;
		}
		return new int[] {dd, mm, yy, ly[i][4]};
	}
	/**
	 * Solar longitude in radians, accuracy of 0.01 degree
	 * Algorithm from: Astronomical Algorithms, by Jean Meeus, 1998
	 * @param jdn
	 * @return
	 */
	public static double SunLongitude(double jdn) {
		double T = (jdn - 2451545.0 ) / 36525; // Time in Julian centuries from 2000-01-01 12:00:00 GMT
		double T2 = T*T;
		double dr = PI/180; // degree to radian
		double M = 357.52910 + 35999.05030*T - 0.0001559*T2 - 0.00000048*T*T2; // mean anomaly, degree
		double L0 = 280.46645 + 36000.76983*T + 0.0003032*T2; // mean longitude, degree
		double DL = (1.914600 - 0.004817*T - 0.000014*T2)*Math.sin(dr*M);
		DL = DL + (0.019993 - 0.000101*T)*Math.sin(dr*2*M) + 0.000290*Math.sin(dr*3*M);
		double L = L0 + DL; // true longitude, degree
		L = L*dr;
		L = L - PI*2*(INT(L/(PI*2))); // Normalize to (0, 2*PI)
		return L;
	}

	/**
	 * Meeus's algorithm for converting Julian date number to dd/mm/yyyy of Julian or Gregorian calendar (at GMT).
	 * It works for any positive Julian date;
	 * @param JD
	 * @return Integer array [day, month, year]
	 */
	public static int[] UniversalFromJD(double JD) {
		int Z, A, alpha, B, C, D, E, dd, mm, yyyy;
		double F;
		Z = INT(JD+0.5);
		F = (JD+0.5)-Z;
		if (Z < 2299161) {
		  A = Z;
		} else {
		  alpha = INT((Z-1867216.25)/36524.25);
		  A = Z + 1 + alpha - INT(alpha/4);
		}
		B = A + 1524;
		C = INT( (B-122.1)/365.25);
		D = INT( 365.25*C );
		E = INT( (B-D)/30.6001 );
		dd = INT(B - D - INT(30.6001*E) + F);
		if (E < 14) {
		  mm = E - 1;
		} else {
		  mm = E - 13;
		}
		if (mm < 3) {
		  yyyy = C - 4715;
		} else {
		  yyyy = C - 4716;
		}
		return new int[]{dd, mm, yyyy};
	}

	/**
	 * Convert D/M/Y 00:00 GMT of Julian or Gregorian calendar to Julian date.
	 * Algorithm found at http://scienceworld.wolfram.com/astronomy/JulianDate.html
	 * @param D
	 * @param M
	 * @param Y
	 * @return
	 */
	public static double UniversalToJD(int D, int M, int Y) {
		double JD;
		if (Y > 1582 || (Y == 1582 && M > 10) || (Y == 1582 && M == 10 && D > 14)) {
			JD = 367*Y - INT(7*(Y+INT((M+9)/12))/4) - INT(3*(INT((Y+(M-9)/7)/100)+1)/4) + INT(275*M/9)+D+1721028.5;
		} else {
			JD = 367*Y - INT(7*(Y+5001+INT((M-9)/7))/4) + INT(275*M/9)+D+1729776.5;
		}
		return JD;
	}
}

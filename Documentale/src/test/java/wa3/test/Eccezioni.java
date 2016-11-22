package wa3.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Eccezioni {

	public Eccezioni() {
	}

	public static class ErroreChecked extends Exception{

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public ErroreChecked() {
			super();
			// TODO Auto-generated constructor stub
		}

		public ErroreChecked(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
			// TODO Auto-generated constructor stub
		}

		public ErroreChecked(String message, Throwable cause) {
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public ErroreChecked(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}

		public ErroreChecked(Throwable cause) {
			super(cause);
			// TODO Auto-generated constructor stub
		}

	}

	public static class ErroreUnchecked extends RuntimeException{

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public ErroreUnchecked() {
			super();
			// TODO Auto-generated constructor stub
		}

		public ErroreUnchecked(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
			// TODO Auto-generated constructor stub
		}

		public ErroreUnchecked(String message, Throwable cause) {
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public ErroreUnchecked(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}

		public ErroreUnchecked(Throwable cause) {
			super(cause);
			// TODO Auto-generated constructor stub
		}

	}


	public static void main(String[] args) {

		//		try {
		//			java6();
		//		} catch (IOException e1) {
		//			e1.printStackTrace();
		//		}
		//
		//		java7();


		Integer numero = null;
		try {
			controllaNumero2(numero);
		} catch (ErroreChecked e) {
			//System.out.println("Terrore!!");
			//			e.printStackTrace();
			throw new ErroreUnchecked(e);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException!!");
		} finally {
			System.out.println("Finito");
			numero = null;
		}
	}

	static void java6() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("local_conf/app.properties"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			System.out.println(everything);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally{
			br.close();
		}

	}

	static void java7(){
		try (BufferedReader br = new BufferedReader(new FileReader("local_conf/app.properties"));)
		{
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			System.out.println(everything);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	static void controllaNumero(Integer numero) {
		if (numero==null){
			throw new IllegalArgumentException("Numero è null");
		}

	}

	private static void controllaNumero2(Integer numero) throws ErroreChecked {
		if (numero==null){
			throw new ErroreChecked("Numero è null");
		}
		if (numero.intValue()<10){
			throw new ErroreUnchecked("Numero è minore di 10");
		}

	}
}

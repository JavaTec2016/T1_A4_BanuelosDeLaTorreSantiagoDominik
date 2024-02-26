import java.util.ArrayList;

public class Calculadora {
	
	double memoria = 0.0;
	double operando = 0.0;
	char operador = ' ';
	ArrayList<String> operaciones = new ArrayList<String>();
	ArrayList resultados = new ArrayList();
	
	double sumar(double n1, double n2) {
		return n1+n2;
	}
	double restar(double n1, double n2) {
		return n1-n2;
	}
	double multiplicar(double n1, double n2) {
		return n1*n2;
	}
	double dividir(double n1, double n2) {
		return n1/n2;
	}
	void limpiar() {
		operador = 0;
		operando = 0;
	}
	void MS(double n) {
		memoria = n;
	}
	double MR() {
		return memoria;
	}
	void Mplus(double n) {
		memoria+=n;
	}
	void Mmenos(double n) {
		memoria-=n;
	}
	boolean esNumerico(char c) {
		return (Character.isDigit(c) || c == '.');
	}
	double identificar(double n) {
		switch (operador) {
		case '+': return sumar(operando, n); 
		case '-': return restar(operando, n);
		case 'X': return multiplicar(operando, n);
		case '/': return dividir(operando, n);
		case '^': return n*n;
		case '√': return Math.sqrt(n);
		case '%': return n/100;
		default:
			return n;
		}
	}
}

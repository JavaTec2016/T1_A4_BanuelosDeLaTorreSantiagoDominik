import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


//accion de botones numericos
class Botoneo implements ActionListener{
	Ventana ref = null;
	public Botoneo(Ventana v) {
		ref = v;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		char num = ((JButton)(e.getSource())).getText().charAt(0);
		//si aun no hay operador o no es un resultado engorda el numero
		if(!ref.enter) {
			System.out.println("aniadido");
			System.out.println(ref.esDecimal());
			if(ref.esDecimal() && num == '.') return;
			ref.anadir(num);
			
			
			//si hay un operador reemplaza el numero y actualiza el operando
		}else {
			ref.establecer(""+num);
			System.out.println("remplaza");
			
		}
		ref.enter = false;
		ref.numero = true;
	}
	
}
//accion de botones de operacion
class Operador implements ActionListener {
	Ventana ref = null;
	public Operador(Ventana v) {
		ref = v;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		ref.enter = true;
		
		char o = ((JButton)(e.getSource())).getText().charAt(0);
		char oPrev = ref.c.operador;
		//si no habia operador, actualiza la pantalla de operacion y el operando
		if(!ref.operador) {
			
			//CASOS ESPECIALES
			//no avisan que hay un operador
			ref.setOperacion(""+ref.numero()+o);
			ref.numero = false;
			ref.c.operando = ref.numero();
			
			
		}else if(!ref.numero){ //si el ultimo boton fue un operador
			//corta el ultimo operador y agrega el nuevo
			
			String op = ref.getOperacion();
			op = op.substring(0, op.length()-2);
			ref.setOperacion(op+o);
			
		}else {//si ya hay un operador y el ultimo boton fue un numero
			//realiza la operacion y prepara la siguiente operacion 
			System.out.println("cadena");
			
			//si no resulta, revierte los datos de la calculadora
			ref.actualizarResultado();
			ref.c.operando = ref.numero();
			ref.setOperacion(ref.numero()+" "+o);
			
		}
		ref.c.operador = o;
		ref.operador = true;	
		
	}
}
class Ventana extends JFrame implements ActionListener, KeyListener{
	Calculadora c;
	//logica de botones
	Botoneo numb = new Botoneo(this);
	Operador oper = new Operador(this);
	
	JTextArea areaPantalla;
	JTextArea areaOperacion;
	boolean enter = false;
	boolean operador = false;
	boolean numero = false;
	int gridMulti = 2;
	
	//layout nulo
	int width = 27;
	int height = 26;
	
	public Ventana(Calculadora c) {
		this.c = c;
		getContentPane().setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(340, 650);
		setLocationRelativeTo(null);
		setTitle("Calculadora");
		setVisible(true);
		
		//etiqueta estandar
		//
		/*hay 6 botones de memoria y 4 columnas de otros botones
		 * 
		 * el minimo comun multiplo de 6 y 4 es 12, por lo que la ventana
		 * tendrá 12 columnas de cuadricula
		 * 	3 para cada boton grande
		 * 	2 para cada botón de memoria
		 * 
		 *hay 6 filas de botones grandes y 1 de botones de memoria
		 * */
		
		//espacios(0, -1, 12);
		
		JLabel txtEstandar = new JLabel("Estándar".toUpperCase());
		dimensionar(txtEstandar, 0, 0, 12, 3);
		font(txtEstandar, 1, 15);
		add(txtEstandar);
		
		
		areaOperacion = new JTextArea(1,1);
		areaOperacion.setEditable(false);
		dimensionar(areaOperacion, 0, 3, 12, 1);
		font(areaOperacion, 0, 16);
		add(areaOperacion);
		
		areaPantalla = new JTextArea(3,1);
		areaPantalla.setEditable(false);
		dimensionar(areaPantalla, 0, 4, 12, 5);
		font(areaPantalla, 1, 25);
		add(areaPantalla);
		
		
		
		Calculadora calc = this.c;
		add(boton("MC", 0, 9, 2, 1, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				calc.memoria = 0.0;
				establecer(0);
				
			}
		}));
		add(boton("MR", 2, 9, 2, 1, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				establecer(calc.MR());
				numero = true;
				enter = true;
			}
		}));
		add(boton("M+", 4, 9, 2, 1, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!esValido()) return;
				calc.Mplus(numero());
				enter = true;
			}
		}));
		add(boton("M-", 6, 9, 2, 1, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!esValido()) return;
				calc.Mmenos(numero());
				enter = true;
			}
		}));
		add(boton("MS", 8, 9, 2, 1, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!esValido()) return;
				calc.MS(numero());
				enter = true;
				
			}
		}));
		add(boton("M~", 10, 9, 2, 1, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				return;
			}
		}));
		//casos especiales, solo necesitan 1 operando
		add(boton("%", 0, 10, 3, 2, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				char op = c.operador;
				double opd = c.operando;
				double act = numero();
				
				if(op == 'X' || op == '/') act /= 100;
				else act = act/100 * opd;
				
				if(opd != 0)setOperacion(""+opd +" "+ op +" "+ act);
				else setOperacion("0");
				establecer(act);
				numero = true;
			}
		}));
		add(boton("√", 3, 10, 3, 2, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				char op = c.operador;
				double opd = c.operando;
				double act = numero();
				c.operador = '√';
				double res = calc.identificar(act); 
				c.operador = op;
				
				if(opd != 0)setOperacion(""+opd +" "+ op +" √("+ act+")");
				else setOperacion("√("+ act+")");
				establecer(res);
				numero = true;
			}
		}));
		add(boton("^", 6, 10, 3, 2, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				char op = c.operador;
				double opd = c.operando;
				double act = numero();
				c.operador = '^';
				double res = calc.identificar(act); 
				c.operador = op;
				
				if(opd != 0)setOperacion(""+opd +" "+ op +" sqr("+ act+")");
				else setOperacion("sqr("+ act+")");
				establecer(res);
				numero = true;
			}
		}));
		
	 //divide 1 entre el resultado de la expresión
		add(boton("1/", 9, 10, 3, 2, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!esValido()) return;
				double n = numero();
				String o = "1 / ("+n+")";
				setOperacion(o);
				
				if(n == 0) establecer("No se puede dividir entre cero");
				else {
					establecer(1/n);
					calc.operaciones.add(o);
					calc.resultados.add((n));
				}
					
				
				
			}
		}));
		add(boton("CE", 0, 12, 3, 2, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!esValido()) {
					c.limpiar();
					setOperacion("");
					
				}else {
					establecer("");
				}
			}
		}));
		add(boton("C", 3, 12, 3, 2, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				establecer("");
				setOperacion("");
				c.limpiar();
			}
		}));
		add(boton("⌫", 6, 12, 3, 2, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String t = areaPantalla.getText();
				areaPantalla.setText(t.substring(0, Math.max(0, t.length()-1)));
				
			}
		}));
		add(boton("/", 9, 12, 3, 2, oper));
		
		add(boton("7", 0, 14, 3, 2, numb));
		add(boton("8", 3, 14, 3, 2, numb));
		add(boton("9", 6, 14, 3, 2, numb));
		add(boton("X", 9, 14, 3, 2, oper));
		
		add(boton("4", 0, 16, 3, 2, numb));
		add(boton("5", 3, 16, 3, 2, numb));
		add(boton("6", 6, 16, 3, 2, numb));
		add(boton("-", 9, 16, 3, 2, oper));
		
		add(boton("1", 0, 18, 3, 2, numb));
		add(boton("2", 3, 18, 3, 2, numb));
		add(boton("3", 6, 18, 3, 2, numb));
		add(boton("+", 9, 18, 3, 2, oper));
		
		add(boton("±", 0, 20, 3, 2, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				establecer(numero()*-1);
			}
		}));
		add(boton("0", 3, 20, 3, 2, numb));
		add(boton(".", 6, 20, 3, 2, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(esDecimal()) return;
				numb.actionPerformed(e);
				
			}
		}));
		add(boton("=", 9, 20, 3, 2, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!esValido()) return;
				double b = numero();
				double opd = c.operando;
				char op = c.operador;
				actualizarResultado();
				setOperacion(""+opd+" "+op+" "+b+" = ");
				enter = true;
				operador = false;
			}
		}));
		
		
	}
	void actualizarResultado() {
		if(!esValido()) return;
		try {
			establecer(c.identificar(numero()));
			//setOperacion(c.operando+" "+c.operador+" "+numero());
			c.operaciones.add(getOperacion());
			c.resultados.add(numero());
			c.operando = numero();
			enter = true;
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	void casoEspecial(ActionEvent e) {
		
		char o = ((JButton)(e.getSource())).getText().charAt(0);
		if(numero) {
			if(operador) {
				
				char oPrev = c.operador;
				double nPrev = c.operando;
				
				c.operando = numero();
				c.operador = o;
				double res = c.identificar(c.operando);
				
				setOperacion(c.operando+" "+c.operador+" "+res);
				c.operando = nPrev;
				c.operador = oPrev;
				actualizarResultado();
			}else {
				
				c.operando = numero();
				c.operador = o;
				double res = c.identificar(c.operando);
				setOperacion(""+o+c.operando);
				res = c.identificar(res);
				actualizarResultado();
			}
			
			
		}
	}
	double numero() {
		return Double.parseDouble(areaPantalla.getText());
	}
	void establecer(double n) {
		areaPantalla.setText(""+n);
	}
	void setOperacion(String t) {
		areaOperacion.setText(t);
	}
	String getOperacion() {
		return areaOperacion.getText();
	}
	boolean esValido() {
		try {
			numero();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	boolean esDecimal() {
		if(!esValido()) return true;
		boolean res = false;
		String ultimos = "0";
		//si tiene punto pero solo lo siguen 0s entonces no es decimal
		for(char c : areaPantalla.getText().toCharArray()) {
			//recolectar los decimales
			if(res) {
				ultimos+=c;
			}
			//ubicar el punto
			if(c == '.') {
				res = true;
				ultimos+=c;
			};
		}
		return (res || Double.parseDouble(ultimos)%1 != 0.0);
	}
	
	void establecer(String n) {
		if(n.startsWith(".")) areaPantalla.setText("0"+n);
		else areaPantalla.setText(n);
	}
	//reemplaza el numero entero si es resultado directo de una operacion
	void anadir(char n) {
		if(!enter) establecer(areaPantalla.getText()+n);
		else {
			establecer(""+n);
		}
	}
	JButton boton(String simbolo, int x, int y, int w, int h, ActionListener a) {
		JButton c = new JButton(simbolo);
		c.setBounds(x*width,y*height,(int)w*width,(int)h*height);
		c.addActionListener(a);
		//c.setFont(new Font("Arial", 0, 16));
		return c;
		
	}
	void dimensionar(JComponent c, int x, int y, int w, int h) {
		c.setBounds(x*width,y*height,(int)w*width,(int)h*height);
	}
	
	void font(JComponent c, int estilo, int tamano) {
		c.setFont(new Font("Arial", estilo, tamano));
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(!esValido()) return;
		actualizarResultado();
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
public class Arranque {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calculadora c = new Calculadora();
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new Ventana(c);
			}
		});
	}

}

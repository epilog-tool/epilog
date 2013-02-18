package pt.gulbenkian.igc.nmd;




import java.awt.*;

import javax.swing.*;


import org.colomoto.logicalmodel.NodeInfo;

import java.util.ArrayList;
import java.util.List;



public class DrawPolygonM extends JPanel  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double height=0.0;
	public static double radius=0.0;
	public  ArrayList<ArrayList<Cell>> cells=new ArrayList<ArrayList<Cell>> ();
	public  int startX;
	public  int startY;
	public  int endX;
	public  int endY;



	public DrawPolygonM(Map frame,int width, int height, String nodeId, int maxId){

		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(600,700));
		frame.setTitle(nodeId);
		frame.setBackground(Color.white);
		Container contentPane = frame.getContentPane();

		contentPane.add(this);

	}

	public static void drawHexagon(int i, int j, Graphics g, Color c) {
		double centerX = 100, centerY = 0, x = 0, y = 0;

		

		if (i % 2 == 0) {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0)
					/ 2;
		} else {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0);
		}

		Polygon polygon2 = new Polygon();

		for (int k = 0; k < 6; k++) {

			x = centerX + radius * Math.cos(k * 2 * Math.PI / 6);
			y = centerY + radius * Math.sin(k * 2 * Math.PI / 6);
			polygon2.addPoint((int) (x), (int) (y));

		}
		
		
		
		
		
		g.setColor(c);
		g.fillPolygon(polygon2);
		g.setColor(Color.black);
		g.drawPolygon(polygon2);
		
		
		
		
	}

	public static void clearHexagon(int i, int j, Graphics g){
		double centerX=0,centerY=0, x=0,y=0;
		

		if(i%2==0){
			centerX=(1.5*radius*(i))+radius;
			centerY=(j)*radius*Math.sqrt(3.0)+radius*Math.sqrt(3.0)/2;
		}
		else{
			centerX=(1.5*radius*(i))+radius;
			centerY=(j)*radius*Math.sqrt(3.0)+radius*Math.sqrt(3.0);
		}

		Polygon polygon2= new Polygon();

		for (int k = 0; k < 6; k++){

			x=centerX+radius * Math.cos(k * 2 * Math.PI / 6);
			y=centerY+radius * Math.sin(k * 2 * Math.PI / 6);
			polygon2.addPoint((int) (x),(int) (y));


		}

		g.setColor(Color.white);
		g.fillPolygon(polygon2);
		g.setColor(Color.black);
		g.drawPolygon(polygon2);
		
			     

	}

	public void paintComponent(Graphics g) {




		int XX=0,YY=0,max=0;
		try{
			XX=MainPanel.getGridWidth();
			YY=MainPanel.getGridHeight();
			max=Math.max(XX, YY);
			System.out.println("max "+max);
		}
		catch(NumberFormatException e){
			System.out.println("java.lang.NumberFormatException: For input string: ''");


		}
		catch(Exception e){

			e.printStackTrace();
		}

		if(XX>0&&YY>0){


			if(YY==max){

				height=500/max;
				height=(500-1*height)/(max);
				radius=height/Math.sqrt(3.0);


			}

			else{
				radius=(500/XX)/2;


				height=radius*Math.sqrt(3.0);
			}
			System.out.println("height: "+height);
			System.out.println("radius: "+radius);
			double x=0,y=0,centerX=radius,centerY=0;


			super.paintComponent(g);
			

			for(int k=0;k<XX;k++){
				g.setColor(Color.black);
				if(k==0)
					centerX=radius;
				else
					centerX=(1.5*radius*(k))+radius;
				if(k%2==0)
					centerY=radius*Math.sqrt(3.0)/2;//h
				else
					centerY=radius*Math.sqrt(3.0);//h+h/2
				for(int j=0;j<YY;j++){

					Polygon polygon2= new Polygon();

					for (int i = 0; i < 6; i++){

						x=centerX+radius * Math.cos(i * 2 * Math.PI / 6);
						y=centerY+radius * Math.sin(i * 2 * Math.PI / 6);
						polygon2.addPoint((int) (x),(int) (y));


					}


					
					// System.out.println(centerY);

					if(cells.size()>0&&cells.get(k).get(j).color1.getRGB()!=Color.white.getRGB()){
						g.setColor(cells.get(k).get(j).color1);
						g.fillPolygon(polygon2);
						g.setColor(Color.black);
						g.drawPolygon(polygon2);
						
						
					}
					else{
						g.setColor(Color.white);
						g.fillPolygon(polygon2);
						g.setColor(Color.black);
						g.drawPolygon(polygon2);
						
						
					}

					if(k%2==0)
						centerY=(j+1+0.5)*radius*Math.sqrt(3.0);
					else
						centerY=(j+2)*radius*Math.sqrt(3.0);

				}

				//System.out.println(centerX);

			}
			//System.out.println("count "+count+ " h "+radius*Math.sqrt(3.0)+ " Y "+centerY+" "+(500-radius*Math.sqrt(3.0))+" nh "+numberHex); 
		}
		else{
			System.out.println("XX e YY têm que ser maiores do que zero");

		}



	}



	public void hexagonsLoadModel(Graphics g,ArrayList<ArrayList<Cell>> cells, MapColorPanel panel,List<NodeInfo> listNodes) {
		int XX=0,YY=0;int max=0;
		int variables=listNodes.size();
		int pos=0;int green=0;int newColor=30;
		Color colors[]={Color.orange,Color.green,Color.blue,Color.pink,Color.yellow,Color.magenta,Color.cyan,Color.red,Color.LIGHT_GRAY,Color.black};



		for(int k=0;k<variables;k++){
			pos=k;
			JCheckBox j=new JCheckBox(listNodes.get(k).getNodeID());
			ColorButton l = new ColorButton(panel);
			JComboBox jc1=new JComboBox();


			green=k*30;
			if(green>255)
				green=255;
			if(k<colors.length){
				l.setBackground(colors[k]);

			}
			else
				l.setBackground(new Color(k,k*5,k*7));

			j.setBackground(Color.white);
			j.setBounds(10, 10+pos*60, 50, 30);

			if(listNodes.get(k).isInput()){

				jc1.setBounds(100, 10+pos*60, 120, 30);
				jc1.addItem("Env input");
				jc1.addItem("Int input");
				JButton btnLoad=new JButton("Load");
				btnLoad.setBounds(230, 10+pos*60, 100, 30);
				panel.add(btnLoad);

			}

			l.setBounds(60, 10+pos*60, 30, 30);
			panel.add(j);panel.add(l);
			panel.add(jc1);
			panel.revalidate();
			panel.repaint();



		}

		try{
			XX=MainPanel.getGridWidth();
			YY=MainPanel.getGridHeight();
			max=Math.max(XX, YY);
			System.out.println("max "+max);
			System.out.println("XX "+XX);
		}
		catch(NumberFormatException e){
			System.out.println("java.lang.NumberFormatException: For input string: ''");


		}
		catch(Exception e){

			e.printStackTrace();
		}

		if(XX>0&&YY>0){
			if(YY==max){

				height=500/max;
				height=(500-1*height)/(max);
				radius=height/Math.sqrt(3.0);


			}

			else{
				radius=(500/XX)/2;


				height=radius*Math.sqrt(3.0);
			}
			System.out.println("height: "+height);
			System.out.println("radius: "+radius);

			double x=0,y=0,centerX=radius,centerY=0;

			//int numberHex=500/((int)(radius*Math.sqrt(3.0))+1);

			//super.paintComponent(g);
			g.setColor(Color.black);


			for(int k=0;k<XX;k++){
				if(k==0)
					centerX=radius;
				else
					centerX=(1.5*radius*(k))+radius;
				if(k%2==0)
					centerY=radius*Math.sqrt(3.0)/2;//h
				else
					centerY=radius*Math.sqrt(3.0);//h+h/2
				for(int j=0;j<YY;j++){

					Polygon polygon2= new Polygon();

					for (int i = 0; i < 6; i++){

						x=centerX+radius * Math.cos(i * 2 * Math.PI / 6);
						y=centerY+radius * Math.sin(i * 2 * Math.PI / 6);
						polygon2.addPoint((int) (x),(int) (y));


					}

					g.setColor(Color.black);
					g.drawPolygon(polygon2);
					// System.out.println(centerY);
					if(cells.get(k).get(j).G0==0){
						g.setColor(Color.blue);
						g.fillPolygon(polygon2);

					}

					if(k%2==0)
						centerY=(j+1+0.5)*radius*Math.sqrt(3.0);
					else
						centerY=(j+2)*radius*Math.sqrt(3.0);

				}

				//System.out.println(centerX);

			}
			//System.out.println("count "+count+ " h "+radius*Math.sqrt(3.0)+ " Y "+centerY+" "+(500-radius*Math.sqrt(3.0))+" nh "+numberHex); 
		}
		else{
			System.out.println("XX e YY têm que ser maiores do que zero");

		}




	}

}
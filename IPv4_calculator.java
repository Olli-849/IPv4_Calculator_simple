package ipv4_subnetz;

import javax.swing.JOptionPane;

public class IPv4_calculator
{
//	public static String ip_adr = "192.168.1.14";  // hardcodierte Adress-Eingabe
	public static String ip_adr = JOptionPane.showInputDialog("Bitte IP-Adresse eingeben:");
//	public static String subn_msk = "255.255.224.0";  // hardcodierte Subnetz-Masken-Eingabe
	public static String subn_msk = JOptionPane.showInputDialog("Bitte Subnetz-Maske eingeben:");
	public static int exp = 0;  // Exponent (und Anzahl der hinteren Nullen der Oktette)
	public static int cidr = 32;  // Start-CIDR
	public static int cnt = 0;  // Zählerhilfe für Schleifen
	
	static int power(int exponent)  // 2er Potenz ohne Import
	{
		int result = 1;
		for (int i = 0; i < exponent; i++) 
		{
			result = 2 * result;
		}
		return result;
	}

	static String toBitString(int x, int bits)
	{
	    String bitString = Integer.toBinaryString(x);
	    int size = bitString.length();
	    StringBuilder strbld = new StringBuilder(bits);
	    if(bits>size)
	    {
	        for(int i=0;i<bits-size;i++)
	        {
	            strbld.append('0');
	        }
	        strbld.append(bitString);
	    }
	    else
	    {
	    	strbld = strbld.append(bitString.substring(size-bits, size));
	    }
	    return strbld.toString();
	}
	
	public static void main(String[] args)
	{
		String[] subAdrListe = subn_msk.split("\\.");  // Punkt als Trennzeichen muß escaped werden
		int[] i_subAdr_list = new int[subAdrListe.length];  // leeres Array für die Aufnahme der Zahlen der IP-Adresse

		for(int j = 0; j < subAdrListe.length; j++)  // Schleife mit Schrittzahl der Index-Anzahl
		{
			i_subAdr_list[j] = Integer.parseInt(subAdrListe[j]);  // Array-Liste nimmt Zahl des jeweiligen Index (Quell-Array) in dieselbe Index-Position (Ziel-Array) auf
//			System.out.println(i_adr_list[i]);
			if(i_subAdr_list[j]==0)  // wenn das Subnetz-Oktett 0 ist
			{
				exp+=8;
			}
			else 
			{
				if(i_subAdr_list[j]==255)
				{
					exp+=0;
				}
				else  // wenn das Subnetz-Oktett ungleich 255 ist
				{
					String wert = Integer.toBinaryString(i_subAdr_list[j]);  // Umwandeln in Binärzahl
//					System.out.println(wert);  // Test-Ausgabe der Binär-Darstellung
					String reverse = new StringBuffer(wert).reverse().toString();  // Reverse Darstellung
//					System.out.println(reverse);  // Test-Ausgabe der umgekehrten Binär-Darstellung
					String[] rvListe = reverse.split("");
					for(int p=0;p<rvListe.length;p++)
					{
						int rv_int = Integer.parseInt(rvListe[p], 2);
//						System.out.println(rv_int);  // zeilenweise Test-Ausgabe der Bits
						if(rv_int==0)  // die Anzahl der Nullen erhöht den Exponenten zu 2
						{
							exp++;
						}
						else
						{
							break;  // sobald eine 1 erreicht wird, bricht die for-Schleife ab, so dass keine eingeschlossenen Nullen mitgezählt werden
						}
					}
				}
			}
		}
//		System.out.println("Exponent: " + exp);  // Test-Ausgabe der Summe nachgestellter Nullen als Exponent zur 2
		cidr -= exp;  // CIDR als Differenz aus 32 und Exponent
		String[] adrListe = ip_adr.split("\\.");
		StringBuilder al_min = new StringBuilder(adrListe.length);
		StringBuilder al_max = new StringBuilder(adrListe.length);
		int[] i_adr_list = new int[adrListe.length];
		for(int i=0;i<adrListe.length;i++)  // Schleife mit Schrittzahl der Index-Anzahl
		{
			i_adr_list[i] = Integer.parseInt(adrListe[i]);  // Array-Liste nimmt Zahl des jeweiligen Index (Quell-Array) in dieselbe Index-Position (Ziel-Array) auf
		}
		for (int i=0;i<i_adr_list.length;i++)  // Schleife mit Schrittzahl der Index-Anzahl
		{
//			System.out.println(i_adr_list[i]);  // Test-Ausgabe der Zahlen
			StringBuffer originalString = new StringBuffer(toBitString(i_adr_list[i],8));
//			System.out.println ("Original String: " + originalString);
			al_min.append(originalString);  // hängt die binären Ketten aneinander
			al_max.append(originalString);  // hängt die binären Ketten aneinander
		}
//		System.out.println("Gesamt: " + al_min);
		al_min.reverse().toString(); // reversen der gesamten Kette
		al_max.reverse().toString(); // reversen der gesamten Kette
//		System.out.println("Reverse: " + al_min);
//		System.out.println("Reverse: " + al_max);
		for(int n=0;n<exp;n++)
		{
			al_min.replace(n,n+1,"0");
			al_max.replace(n,n+1,"1");
		}
		al_min.reverse().toString(); // zurück reversen der Kette
		al_max.reverse().toString(); // zurück reversen der Kette
//		System.out.println ("Modified String min: " + al_min);
//		System.out.println ("Modified String max: " + al_max);
		String[] newAdrListe_min = al_min.toString().split("",al_min.length());
		String[] newAdrListe_max = al_max.toString().split("",al_max.length());
		StringBuffer oktett_min = new StringBuffer();
		StringBuffer oktett_max = new StringBuffer();
		String[] oktettListe_min = new String[(al_min.length()/8)];
		String[] oktettListe_max = new String[(al_max.length()/8)];
		for(int m=al_min.length();m>0;m-=8)
		{
			for(int k=cnt*8;k<(cnt+1)*8;k++)
			{
				oktett_min.append(newAdrListe_min[k]);
				oktett_max.append(newAdrListe_max[k]);
//				System.out.println(newAdrListe[k]);
			}
//			System.out.println("Oktett: " + oktett_min);
//			System.out.println("Oktett: " + oktett_max);
			oktettListe_min[cnt] = oktett_min.toString();
			oktettListe_max[cnt] = oktett_max.toString();
			oktett_min.setLength(0);
			oktett_max.setLength(0);
			cnt++;
		}
		int[] i_newListe_min = new int[oktettListe_min.length];
		int[] i_newListe_max = new int[oktettListe_max.length];
		StringBuilder builder_min = new StringBuilder();
		StringBuilder builder_max = new StringBuilder();
		for(int q=0;q<oktettListe_min.length;q++)
		{
			i_newListe_min[q] = Integer.parseInt(oktettListe_min[q],2);  // wandelt Array-Inhalt zur Basis 2 an Position q in Integer um und positioniert diesen dann in neues Array an Position q
			i_newListe_max[q] = Integer.parseInt(oktettListe_max[q],2);  // wandelt Array-Inhalt zur Basis 2 an Position q in Integer um und positioniert diesen dann in neues Array an Position q
//			System.out.println(i_newListe_min[q]);
//			System.out.println(i_newListe_max[q]);
			builder_min.append(i_newListe_min[q]);
			builder_max.append(i_newListe_max[q]);
			if(q<oktettListe_min.length-1)
			{
				builder_min.append(".");
				builder_max.append(".");
			}
		}
		System.out.println("IP-Adresse: " + ip_adr + " /" + cidr);
		System.out.println("Subnetz-Maske: " + subn_msk);
//		System.out.println("CIDR: /" + cidr);
		System.out.println("\nGesamtanzahl der Adressen: " + power(exp));
		System.out.println("Host-Anteil: " + (power(exp)-2));
		System.out.println("\nNetz-ID: " + builder_min);
		System.out.println("Broadcast: " + builder_max);
//		System.out.println("Erste Host-Adresse: " + builder_min);
//		System.out.println("Letzte Host-Adresse: " + builder_max);
	}
}

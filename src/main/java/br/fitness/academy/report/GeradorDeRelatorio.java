package br.fitness.academy.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GeradorDeRelatorio {
	
	private Document doc = new Document(PageSize.A4, 25, 25, 25, 25);
	
	public void newRelatorio(String arquivo) throws IOException,DocumentException {
		
	 String caminho = System.getProperty("user.dir") +
	 "\\src\\main\\resources\\static\\relatorios\\" + arquivo;
	 
	 PdfWriter.getInstance(doc, new FileOutputStream(caminho));
	 
	 doc.open();
	}
	
	public void addNewObject(Element[] objetos) throws DocumentException {
		
		for(int i = 0; i < objetos.length; i++) {
			doc.add(objetos[i]);
		}
	 }
	
	public void cabecalho(String titulo, String subTitulo) throws IOException, DocumentException {
		Font textoTitulo1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
		Font textoTitulo2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
		Paragraph tituloCabecalho = new Paragraph(titulo, textoTitulo1);
		tituloCabecalho.setAlignment(Element.ALIGN_CENTER);
		Paragraph subTituloCabecalho = new Paragraph(subTitulo,textoTitulo2);
		subTituloCabecalho.setAlignment(Element.ALIGN_CENTER);
		Element[] objetos = new Element[] {tituloCabecalho,subTituloCabecalho, new Paragraph(" ")};
		addNewObject(objetos);
	}
	
	public void createTabela(String[] args, StringBuilder dados) throws DocumentException {
		PdfPTable tabela = new PdfPTable(args.length);
		
		String[] dt = dados.toString().split("\n");
		int linhas = dt.length;
		int colunas = dt[0].replace(",", "").length();
		String[][] dt2 = new String[linhas][colunas];
		int linhaAtual = 0;
		int colunaAtual = 0;
		for(String linha : dt) {
			String[] elementos = linha.split(",");
			for(String elemento : elementos) {
				dt2[linhaAtual][colunaAtual] = elemento;
				colunaAtual++;
			}
			linhaAtual++;
			colunaAtual = 0;
		}

		for(int i = 0; i < args.length; i++) {
			tabela.addCell(args[i]);
		}
		
		for(int j = 0; j < linhas; j++) {
			for(int k = 0; k < args.length; k++) {
				tabela.addCell(dt2[j][k]);
			}
		}
		Element[] objetos = new Element[] {tabela};
		addNewObject(objetos);
	}
	
	public void qrcode(String dados, int posLinha, int posColuna, int escala) throws DocumentException {
		BarcodeQRCode qrcode = new BarcodeQRCode(dados.trim(), 1, 1,null);
		Image qrcodeImage = qrcode.getImage();
		qrcodeImage.setAbsolutePosition(posColuna, posLinha);
		qrcodeImage.scalePercent(escala);
		Element[] objetos = new Element[] {qrcodeImage};
		addNewObject(objetos);
	}
	
	public void imagem(String nomeRelatorio,String nomeImagem, int posLinha, int posColuna, int escalaX, int escalaY)
		throws MalformedURLException, IOException,DocumentException {
		newRelatorio(nomeRelatorio);
		
		String caminho = System.getProperty("user.dir") +
		"\\src\\main\\resources\\static\\image\\" + nomeImagem;
		Image imagem = Image.getInstance(caminho);
		imagem.scaleToFit(escalaX, escalaY);
		imagem.setAlignment(Element.ALIGN_CENTER);
		Element[] objetos = new Element[] {imagem};
		addNewObject(objetos);
	}
	
	public void rodape() throws DocumentException {
		int pagina = doc.getPageNumber() + 1;
		String p = "Página " + pagina;
		Font textoTitulo2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD,14, BaseColor.BLACK);
		Paragraph rodape = new Paragraph(p, textoTitulo2);
		rodape.setAlignment(Element.ALIGN_CENTER);
		doc.add(new Paragraph(rodape));
		doc.close();
	}
}

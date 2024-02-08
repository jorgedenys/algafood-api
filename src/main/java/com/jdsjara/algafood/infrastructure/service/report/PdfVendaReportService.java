package com.jdsjara.algafood.infrastructure.service.report;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdsjara.algafood.domain.filter.VendaDiariaFilter;
import com.jdsjara.algafood.domain.service.VendaQueryService;
import com.jdsjara.algafood.domain.service.VendaReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PdfVendaReportService implements VendaReportService {

	@Autowired
	private VendaQueryService vendaQueryService;
	
	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
		try {
			// Pegar um inputStream. Pegar o fluxo de dados no arquivo do meu projeto.
			// Busca um recurso dentro do classpath do projeto e retornar como um inputStream
			var inputStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper");
			
			var parametros = new HashMap<String, Object>();
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
			
			var vendasDiarias = vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
			var dataSource = new JRBeanCollectionDataSource(vendasDiarias);
			
			var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);
			
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			throw new ReportException("Não foi possível emitir relatório de vendas diárias", e);
		}
	}

}

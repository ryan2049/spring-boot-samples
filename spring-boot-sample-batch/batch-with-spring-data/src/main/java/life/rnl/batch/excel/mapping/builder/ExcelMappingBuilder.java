package life.rnl.batch.excel.mapping.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import life.rnl.batch.excel.annotations.ExcelColumn;
import life.rnl.batch.excel.annotations.ExcelSheet;
import life.rnl.batch.excel.mapping.ExcelMapping;
import life.rnl.batch.excel.mapping.ExcelMappingColumn;
import life.rnl.batch.excel.mapping.ExcelMappingImpl;

public interface ExcelMappingBuilder {
	
	default ExcelMapping build(Class<?> clazz) {
		Assert.notNull(clazz, "A class must be specified to build the ExcelMapping.");

		ExcelMappingImpl excelMapping = new ExcelMappingImpl();

		ExcelSheet excelSheet = clazz.getAnnotation(ExcelSheet.class);
		Assert.notNull(excelSheet,
				String.format("An @ExcelSheet annotation must be specified on the %s class", clazz.getCanonicalName()));
		excelMapping.setSheetName(excelSheet.value());

		List<ExcelMappingColumn> columns = new ArrayList<>();
		ReflectionUtils.doWithFields(clazz, (field) -> {
			ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
			columns.add(new ExcelMappingColumn(field.getName(), excelColumn.value()));
		}, (field) -> {
			ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
			if (excelColumn == null) {
				return false;
			}
			return true;
		});
		Assert.notEmpty(columns, "Some fields within %s must be annotated with @ExcelColumn.");
		excelMapping.getColumns().addAll(columns);

		return excelMapping;
	}
}

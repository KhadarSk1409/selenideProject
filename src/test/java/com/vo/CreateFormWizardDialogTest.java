package com.vo;

import com.vo.createformdialog.CreateFormWithoutAdditionalOptionsTest;
import com.vo.createformdialog.DataCapturePropertiesTest;
import com.vo.createformdialog.LanguagePropertiesTest;
import com.vo.createformdialog.PublicationPropertiesTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

@DisplayName("Create Form Wizard Dialog Tests")
public class CreateFormWizardDialogTest {

    @Nested
    @DisplayName("Main Properties")
    class _CreateFormWithoutAdditionalOptionsTest extends CreateFormWithoutAdditionalOptionsTest {}

    @Nested
    @DisplayName("Language Properties")
    class _LanguagePropertiesTest extends LanguagePropertiesTest {}

    @Nested
    @DisplayName("Publication Properties")
    class _PublicationPropertiesTest extends PublicationPropertiesTest {}

    @Nested
    @DisplayName("Data Capture Properties")
    class _DataCapturePropertiesTest extends DataCapturePropertiesTest {}
}

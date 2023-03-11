package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.handlers.ThrowValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    public void givenValidParams_whenCallNewCategory_thenInstantiateCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError(){
        final String expectedName = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        var actualException
                = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowValidationHandler()));

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError(){
        final String expectedName = "  ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        var actualException
                = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowValidationHandler()));

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError(){
        final String expectedName = "Fi ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        var actualException
                = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowValidationHandler()));

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError(){
        final String expectedName = """
NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255
NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255
NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255
NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255
NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255
NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255NomeDeUsuarioMoreThan255
""" ;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        var actualException
                = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowValidationHandler()));

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidEmptyDescription_whenCallNewCategoryAndValidate_thenInstantiateCategory(){
        final String expectedName = "Filmes";
        final var expectedDescription = "";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> actualCategory.validate(new ThrowValidationHandler()));
    }

    @Test
    public void givenAValidFalseIsActive_whenCallNewCategoryAndValidate_thenInstantiateCategory(){
        final String expectedName = "Filmes";
        final var expectedDescription = "";
        final var expectedIsActive = false;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> actualCategory.validate(new ThrowValidationHandler()));
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidActiveCategory_whenCallDeactivate_thenReturnInactivatedCategory() throws InterruptedException {
        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> actualCategory.validate(new ThrowValidationHandler()));

        final var updatedAt = actualCategory.getUpdatedAt();

        assertNull(actualCategory.getDeletedAt());
        assertTrue(actualCategory.getActive());


        Thread.sleep(200);
        final var deactivatedCategory = actualCategory.deactivate();

        assertNotNull(actualCategory.getId());
        assertFalse(deactivatedCategory.getActive());
        assertEquals(expectedName, actualCategory.getName());
        assertNotNull(deactivatedCategory.getDeletedAt());
        assertTrue(deactivatedCategory.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    public void givenAValidDeactivatedCategory_whenCallActivate_thenReturnActivatedCategory() throws InterruptedException {
        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> actualCategory.validate(new ThrowValidationHandler()));

        final var updatedAt = actualCategory.getUpdatedAt();

        assertNotNull(actualCategory.getDeletedAt());
        assertFalse(actualCategory.getActive());

        Thread.sleep(200);
        final var activatedCategory = actualCategory.activate();


        assertNotNull(actualCategory.getId());
        assertTrue(activatedCategory.getActive());
        assertTrue(activatedCategory.getUpdatedAt().isAfter(updatedAt));
        assertEquals(expectedName, actualCategory.getName());
        assertNull(activatedCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated() throws InterruptedException {
        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Los Filmes", "La Description", expectedIsActive);

        assertDoesNotThrow(() -> aCategory.validate(new ThrowValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();
        Thread.sleep(200);
        final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        assertNotNull(actualCategory.getId());
        assertTrue(actualCategory.getActive());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdateToInactive_thenReturnCategoryUpdated() throws InterruptedException {
        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var aCategory = Category.newCategory("Los Filmes", "La Description", true);

        assertDoesNotThrow(() -> aCategory.validate(new ThrowValidationHandler()));
        assertNull(aCategory.getDeletedAt());
        assertTrue(aCategory.getActive());
        final var updatedAt = aCategory.getUpdatedAt();

        Thread.sleep(200);
        final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        assertNotNull(actualCategory.getId());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertFalse(actualCategory.getActive());
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdateWithInvalidValue_thenReturnCategoryUpdated() throws InterruptedException {
        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Los Filmes", "La Description", true);

        assertDoesNotThrow(() -> aCategory.validate(new ThrowValidationHandler()));
        assertNull(aCategory.getDeletedAt());
        assertTrue(aCategory.getActive());
        final var updatedAt = aCategory.getUpdatedAt();

        Thread.sleep(200);
        final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        assertNotNull(actualCategory.getId());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertTrue(aCategory.getActive());
        assertNull(actualCategory.getDeletedAt());
    }
}

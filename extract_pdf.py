import PyPDF2
import sys

def extract_text_from_pdf(pdf_path):
    try:
        with open(pdf_path, 'rb') as file:
            pdf_reader = PyPDF2.PdfReader(file)
            text = ""
            for page_num in range(len(pdf_reader.pages)):
                page = pdf_reader.pages[page_num]
                text += page.extract_text()
            return text
    except Exception as e:
        return f"Error al leer el PDF: {str(e)}"

if __name__ == "__main__":
    pdf_path = "Prueba técnica 2025.pdf"
    text = extract_text_from_pdf(pdf_path)
    with open("pdf_content.txt", "w", encoding="utf-8") as f:
        f.write(text)
    print("Contenido del PDF extraído y guardado en pdf_content.txt")

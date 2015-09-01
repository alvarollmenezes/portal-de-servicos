
package br.gov.servicos.v3.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Etapa complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Etapa"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="titulo" type="{http://servicos.gov.br/v3/schema}Titulo" minOccurs="0"/&gt;
 *         &lt;element name="descricao" type="{http://servicos.gov.br/v3/schema}DescricaoDaEtapa" minOccurs="0"/&gt;
 *         &lt;element name="documentos" type="{http://servicos.gov.br/v3/schema}Documentos" minOccurs="0"/&gt;
 *         &lt;element name="custos" type="{http://servicos.gov.br/v3/schema}Custos" minOccurs="0"/&gt;
 *         &lt;element name="canais-de-prestacao" type="{http://servicos.gov.br/v3/schema}CanaisDePrestacao" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Etapa", propOrder = {
    "titulo",
    "descricao",
    "documentos",
    "custos",
    "canaisDePrestacao"
})
public class Etapa {

    protected String titulo;
    protected String descricao;
    protected Documentos documentos;
    protected Custos custos;
    @XmlElement(name = "canais-de-prestacao")
    protected CanaisDePrestacao canaisDePrestacao;

    /**
     * Gets the value of the titulo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Sets the value of the titulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitulo(String value) {
        this.titulo = value;
    }

    /**
     * Gets the value of the descricao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Sets the value of the descricao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricao(String value) {
        this.descricao = value;
    }

    /**
     * Gets the value of the documentos property.
     * 
     * @return
     *     possible object is
     *     {@link Documentos }
     *     
     */
    public Documentos getDocumentos() {
        return documentos;
    }

    /**
     * Sets the value of the documentos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Documentos }
     *     
     */
    public void setDocumentos(Documentos value) {
        this.documentos = value;
    }

    /**
     * Gets the value of the custos property.
     * 
     * @return
     *     possible object is
     *     {@link Custos }
     *     
     */
    public Custos getCustos() {
        return custos;
    }

    /**
     * Sets the value of the custos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Custos }
     *     
     */
    public void setCustos(Custos value) {
        this.custos = value;
    }

    /**
     * Gets the value of the canaisDePrestacao property.
     * 
     * @return
     *     possible object is
     *     {@link CanaisDePrestacao }
     *     
     */
    public CanaisDePrestacao getCanaisDePrestacao() {
        return canaisDePrestacao;
    }

    /**
     * Sets the value of the canaisDePrestacao property.
     * 
     * @param value
     *     allowed object is
     *     {@link CanaisDePrestacao }
     *     
     */
    public void setCanaisDePrestacao(CanaisDePrestacao value) {
        this.canaisDePrestacao = value;
    }

}

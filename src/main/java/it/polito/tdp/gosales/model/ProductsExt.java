package it.polito.tdp.gosales.model;

import java.util.Objects;

public class ProductsExt implements Comparable<ProductsExt>{
	private Products prodotto;
	private Double ricavo;
	private int NArchiEntranti;
	
	public ProductsExt(Products prodotto, double ricavo, int NArchiEntranti) {
		super();
		this.prodotto = prodotto;
		this.ricavo = ricavo;
		this.NArchiEntranti = NArchiEntranti;
	}


	public Products getProdotto() {
		return prodotto;
	}



	public void setProdotto(Products prodotto) {
		this.prodotto = prodotto;
	}



	public Double getRicavo() {
		return ricavo;
	}



	public void setRicavo(Double ricavo) {
		this.ricavo = ricavo;
	}



	public int getNArchiEntranti() {
		return NArchiEntranti;
	}

	

	@Override
	public int hashCode() {
		return Objects.hash(NArchiEntranti, prodotto, ricavo);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductsExt other = (ProductsExt) obj;
		return NArchiEntranti == other.NArchiEntranti && Objects.equals(prodotto, other.prodotto)
				&& Objects.equals(ricavo, other.ricavo);
	}


	public void setNArchiEntranti(int NArchiEntranti) {
		this.NArchiEntranti = NArchiEntranti;
	}



	@Override
	public String toString() {
		return prodotto + "  \tArchi Entranti = " + this.NArchiEntranti  + " \tRicavo = " + ricavo;
	}
	
	public String toStringReduced() {
		return prodotto + "   \tRicavo = " + ricavo;
	}


	@Override
	public int compareTo(ProductsExt o) {
		// TODO Auto-generated method stub
		return o.NArchiEntranti - this.NArchiEntranti;
	}
	
	
	
}

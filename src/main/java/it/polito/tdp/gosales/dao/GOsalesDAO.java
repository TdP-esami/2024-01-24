package it.polito.tdp.gosales.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.gosales.model.DailySale;
import it.polito.tdp.gosales.model.Methods;
import it.polito.tdp.gosales.model.Products;
import it.polito.tdp.gosales.model.Retailers;

public class GOsalesDAO {
	
	
	/**
	 * Metodo per leggere la lista di tutti i rivenditori dal database
	 * @return
	 */

	public List<Retailers> getAllRetailers(){
		String query = "SELECT * from go_retailers";
		List<Retailers> result = new ArrayList<Retailers>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Retailers(rs.getInt("Retailer_code"), 
						rs.getString("Retailer_name"),
						rs.getString("Type"), 
						rs.getString("Country")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
	/**
	 * Metodo per leggere la lista di tutti i prodotti dal database
	 * @return
	 */
	public List<Products> getAllProducts(){
		String query = "SELECT * from go_products";
		List<Products> result = new ArrayList<Products>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Products(rs.getInt("Product_number"), 
						rs.getString("Product_line"), 
						rs.getString("Product_type"), 
						rs.getString("Product"), 
						rs.getString("Product_brand"), 
						rs.getString("Product_color"),
						rs.getDouble("Unit_cost"), 
						rs.getDouble("Unit_price")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	
	/**
	 * Metodo per leggere la lista di tutte le vendite nel database
	 * @return
	 */
	public List<DailySale> getAllSales(){
		String query = "SELECT * from go_daily_sales";
		List<DailySale> result = new ArrayList<DailySale>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new DailySale(rs.getInt("retailer_code"),
				rs.getInt("product_number"),
				rs.getInt("order_method_code"),
				rs.getTimestamp("date").toLocalDateTime().toLocalDate(),
				rs.getInt("quantity"),
				rs.getDouble("unit_price"),
				rs.getDouble("unit_sale_price")  ));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	public List<Integer> getYears(){
		String query = "SELECT DISTINCT YEAR(date) as anno FROM go_daily_sales";
		List<Integer> result = new ArrayList<Integer>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(rs.getInt("anno"));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
	public List<Methods> getMethods(){
		String query = "SELECT * FROM go_methods";
		List<Methods> result = new ArrayList<Methods>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add( new Methods(rs.getInt("Order_method_code"), rs.getString("Order_method_type")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
	public List<Products> getVertici(Methods metodo, Integer anno){
		String query = "SELECT DISTINCT P.*"
				+ "FROM go_daily_sales S, go_products P "
				+ "WHERE S.Product_number = P.Product_number AND S.Order_method_code=? AND YEAR(S.Date) = ?";
		List<Products> result = new ArrayList<Products>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, metodo.getCode());
			st.setInt(2, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Products(rs.getInt("P.Product_number"), 
						rs.getString("P.Product_line"), 
						rs.getString("P.Product_type"), 
						rs.getString("P.Product"), 
						rs.getString("P.Product_brand"), 
						rs.getString("P.Product_color"),
						rs.getDouble("P.Unit_cost"), 
						rs.getDouble("P.Unit_price")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
	public Map<Integer, Double> getRicavoProdotti(Methods metodo, Integer anno){
		String query = "SELECT P.Product_number, SUM(S.Unit_sale_price*S.Quantity) as ricavo "
				+ "FROM go_daily_sales S, go_products P "
				+ "WHERE S.Product_number = P.Product_number AND S.Order_method_code=? AND YEAR(S.Date) = ? "
				+ "GROUP BY P.Product_number";
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, metodo.getCode());
			st.setInt(2, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.put(rs.getInt("P.Product_number"), rs.getDouble("ricavo"));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
}

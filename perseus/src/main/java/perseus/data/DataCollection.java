package perseus.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import perseus.Application;

@Controller
public class DataCollection {
	
	@Autowired
	private DataSource dataSource;
	
	public List<String[]> getNarrativeScenarios(int idNarrative) {
		List<String[]> data = new ArrayList<String[]>();
		try {
			Connection conn = dataSource.getConnection();
			// Get top keywords for a given scenario
			String query = "SELECT id_scenario, title FROM scenario WHERE id_narrative = ?";
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, idNarrative);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				ArrayList<String> row = new ArrayList<String>();
				String[] outputRow = new String[row.size()];
				
				row.add("" + resultSet.getInt(1));
				row.add(resultSet.getString(2));
				
				outputRow = row.toArray(outputRow);
	        	data.add(outputRow);
			}
        	conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public List<Object[]> getNarrative() {
		String lang = "en";
		List<Object[]> data = new ArrayList<Object[]>();
		
		try {
			Connection conn = dataSource.getConnection();
			
			String query = "SELECT id_narrative, name FROM narrative WHERE lang = ?";
			
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, lang);
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				ArrayList<Object> row = new ArrayList<Object>();
				
				row.add("" + resultSet.getString(1));
				row.add("" + resultSet.getString(2));
				
				int id_narrative = resultSet.getInt(1);
				
				String subQuery = "SELECT img_path FROM scenario WHERE id_narrative = ? LIMIT 1";
				
				PreparedStatement subStatement = conn.prepareStatement(subQuery);
				subStatement.setInt(1, id_narrative);
				
				ResultSet subResultSet = subStatement.executeQuery();
				
				while(subResultSet.next()) {
					row.add(subResultSet.getBlob(1));
				}
				
				Object[] outputRow = new Object[row.size()];
				outputRow = row.toArray(outputRow);
	        	
	        	data.add(outputRow);
			}

        	conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public List<Object[]> getNarrative(int idNarrative) {
		String lang = "en";
		List<Object[]> data = new ArrayList<Object[]>();
		
		try {
			Connection conn = dataSource.getConnection();
			
			String query = "SELECT id_narrative, name FROM narrative WHERE lang = ? AND id_narrative = ?";
			
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, lang);
			statement.setInt(2, idNarrative);
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				ArrayList<Object> row = new ArrayList<Object>();
				
				row.add("" + resultSet.getString(1));
				row.add("" + resultSet.getString(2));
				
				Object[] outputRow = new Object[row.size()];
				outputRow = row.toArray(outputRow);
	        	
	        	data.add(outputRow);
			}

        	conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUserNumber", method = RequestMethod.GET)
	public int getUserNumber(int idScenario) {
		String lang = "en";
		Integer users = 0;
		
		try {
			Connection conn = dataSource.getConnection();
			
			String query = "SELECT COUNT(DISTINCT T.cookie) FROM"
						+ " (SELECT m.player AS 'cookie' FROM (matchmaking AS m JOIN vision AS v ON m.vision_challenger = v.id_vision) JOIN scenario AS s ON s.id_scenario = v.scenario WHERE s.id_scenario = ? AND s.lang = ?" //match played
						+ "	UNION ALL"
						+ "	SELECT v.cookie AS 'cookie' FROM vision AS v JOIN scenario AS s ON s.id_scenario = v.scenario WHERE s.id_scenario = ? AND s.lang = ?" //vision created
						+ " UNION ALL"
						+ " SELECT a.cookie AS 'cookie' FROM ((quiz AS q JOIN question AS qu ON q.id_quiz = qu.id_quiz) JOIN answer AS a on qu.id_question = a.id_question) JOIN scenario AS s ON s.quiz = q.id_quiz WHERE s.id_scenario = ? AND qu.language = ?) AS T"; //quiz answered
			
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, idScenario);
			statement.setString(2, lang);
			statement.setInt(3, idScenario);
			statement.setString(4, lang);
			statement.setInt(5, idScenario);
			statement.setString(6, lang);
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				users = resultSet.getInt(1);
			}
			
        	conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return users.intValue();
	}

	@ResponseBody
	@RequestMapping(value = "/getQuizAnswersData", method = RequestMethod.GET)
	public List<Object[]> getQuizAnswersData(int idScenario, String genderFilter, int ageFilter, String nationalityFilter) {

		String lang = "en";

		List<Object[]> data = new ArrayList<Object[]>();
		
		try {
			Connection conn = dataSource.getConnection();
			
			int cond_count = 0;
			
			String query = "SELECT i.*, q.question_text FROM (SELECT id_question, answer_number, count(*) FROM answer AS a JOIN user AS u ON a.cookie = u.cookie ";
			
			if(!genderFilter.equals("0") || ageFilter != 0 || !nationalityFilter.equals("0")) {
				query += "WHERE ";
			}
			
			if(!genderFilter.equals("0")) {
				query += "u.gender = ? ";
				cond_count++;
			}
			
			if(ageFilter != 0) {
				if(cond_count > 0) {
					query += "AND ";
				}
				query += "u.age = ? ";
				cond_count++;
			}
			
			if(!nationalityFilter.equals("0")) {
				if(cond_count > 0) {
					query += "AND ";
				}
				query += "u.nationality = ? ";
				cond_count++;
			}
			
			query += "GROUP BY id_question, answer_number UNION ALL SELECT id_question, n, 0 FROM question, (SELECT 1 as n UNION ALL SELECT 2 as n UNION ALL SELECT 3 as n UNION ALL SELECT 4 as n UNION ALL SELECT 5 as n) t WHERE (id_question, n) NOT IN (SELECT q.id_question, a.answer_number FROM (question AS q JOIN answer AS a ON q.id_question = a.id_question) JOIN user AS u ON a.cookie = u.cookie ";
			
			cond_count = 0;
			
			if(!genderFilter.equals("0") || ageFilter != 0 || !nationalityFilter.equals("0")) {
				query += "WHERE ";
			}
			
			if(!genderFilter.equals("0")) {
				query += "u.gender = ? ";
				cond_count++;
			}
			
			if(ageFilter != 0) {
				if(cond_count > 0) {
					query += "AND ";
				}
				query += "u.age = ? ";
				cond_count++;
			}
			
			if(!nationalityFilter.equals("0")) {
				if(cond_count > 0) {
					query += "AND ";
				}
				query += "u.nationality = ? ";
				cond_count++;
			}
			
			query += "GROUP BY q.id_question, a.answer_number HAVING count(DISTINCT a.answer_number) < 5)) i JOIN question AS q on i.id_question = q.id_question JOIN quiz AS qu ON qu.id_quiz = q.id_quiz WHERE q.id_quiz IN (SELECT quiz FROM scenario WHERE id_scenario = ?) AND q.id_question IN (SELECT id_question FROM question WHERE language = ?) ORDER BY i.id_question, i.answer_number";
			
			int ps_count = 1;
			
			PreparedStatement statement = conn.prepareStatement(query);
			for (int i = 0; i < 2; i++) {
				if(!genderFilter.equals("0")) {
					statement.setString(ps_count, genderFilter);
					ps_count++;
				}
				
				if(ageFilter != 0) {
					statement.setInt(ps_count, ageFilter);
					ps_count++;
				}
				
				if(!nationalityFilter.equals("0")) {
					statement.setString(ps_count, nationalityFilter);
					ps_count++;
				}
			}
			
			statement.setInt(ps_count, idScenario);
			ps_count++;
			statement.setString(ps_count, lang);
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				ArrayList<Object> row = new ArrayList<Object>();
				
				row.add(resultSet.getInt(1));
				
				String word = "";
				switch(resultSet.getInt(2)) {
					case 1:
						word = "Strongly \n Disagree";
						break;
					case 2:
						word = "Disagree";
						break;
					case 3:
						word = "Impartial";
						break;
					case 4:
						word = "Agree";
						break;
					case 5:
						word = "Strongly \n Agree";
						break;
				}
				row.add(word);
				
				row.add(resultSet.getInt(3));
				row.add(resultSet.getString(4));
				
				switch(resultSet.getInt(2)) {
					case 1:
						row.add("color: #ed4848");
						break;
					case 2:
						row.add("color: #ff7800");
						break;
					case 3:
						row.add("color: #ffc400");
						break;
					case 4:
						row.add("color: #76d585");
						break;
					case 5:
						row.add("color: #42c157");
						break;
				}
				
				Object[] outputRow = new Object[row.size()];
				outputRow = row.toArray(outputRow);
	        	
	        	data.add(outputRow);
			}

        	conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getFeelings", method = RequestMethod.GET)
	public List<Object[]> getFeelings(int idScenario) {

		String lang = "en";
		List<Object[]> data = new ArrayList<Object[]>();
		
		try {
			Connection conn = dataSource.getConnection();
			
			String query = "SELECT feelings, COUNT(*) AS 'count' FROM vision AS v JOIN scenario AS s ON v.scenario = s.id_scenario WHERE v.scenario = ? AND s.lang = ? GROUP BY feelings ORDER BY count";
			
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, idScenario);
			statement.setString(2, lang);
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				ArrayList<Object> row = new ArrayList<Object>();
				Object[] outputRow = new Object[row.size()];
				
				row.add(sentimentMapper(resultSet.getInt(1)));
				row.add(resultSet.getInt(2));
				
				outputRow = row.toArray(outputRow);
	        	data.add(outputRow);
			}
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	private String sentimentMapper(int sentimentNumber){
		String word = "";
		switch(sentimentNumber) {
			case 1:
				word = "Joy";
				break;
			case 2:
				word = "Trust";
				break;
			case 3:
				word = "Fear";
				break;
			case 4:
				word = "Surprise";
				break;
			case 5:
				word = "Sadness";
				break;
			case 6:
				word = "Disgust";
				break;
			case 7:
				word = "Anger";
				break;
			case 8:
				word = "Anticipation";
				break;
		}
		return word;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getVisionCountOverTime", method = RequestMethod.GET)
	public List<Object[]> getVisionCountOverTime(int idScenario, String startDate, String endDate) {

		String lang = "en";
		List<Object[]> data = new ArrayList<Object[]>();
		
		try {
			Connection conn = dataSource.getConnection();
			
			String query = "SELECT v.share_date, COUNT(id_vision) FROM vision AS v JOIN scenario AS s ON v.scenario = s.id_scenario WHERE v.scenario = ? AND s.lang = ? ";
			
			if(startDate != "") {
				query += "AND v.share_date > ?";
			}
			
			if(endDate != "") {
				query += "AND v.share_date < ?";
			}
			
			query += "GROUP BY v.share_date";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setInt(1, idScenario);
			statement.setString(2, lang);
			
			int ps_count = 3;
			
			if(startDate != "") {
				statement.setDate(ps_count, Date.valueOf(startDate));
				ps_count++;
			}
			
			if(endDate != "") {
				statement.setDate(ps_count, Date.valueOf(endDate));
			}
			
			LocalDate startDateTmp = LocalDate.parse(startDate.substring(0, 10));
			
			ArrayList<ArrayList<Object>> output = new ArrayList<ArrayList<Object>>();
			ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
			while(!startDateTmp.equals(LocalDate.parse(endDate.substring(0, 10)))) {
				ArrayList<Object> row = new ArrayList<Object>(Arrays.asList(startDateTmp.toString(), 0));
				
				dates.add(startDateTmp);
				output.add(row);
				
				startDateTmp = startDateTmp.plusDays(1);
			}
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				output.set(dates.indexOf(LocalDate.parse(resultSet.getString(1).substring(0, 10))), new ArrayList<Object>(Arrays.asList(resultSet.getDate(1), resultSet.getInt(2))));
			}
			
			for(ArrayList<Object> row: output) {
				Object[] outputRow = new Object[row.size()];
				data.add(row.toArray(outputRow));
			}
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getVisionFeelingsOverTime", method = RequestMethod.GET)
	public List<Object[]> getVisionFeelingsOverTime(int idScenario, String startDate, String endDate) {

		String lang = "en";
		List<Object[]> data = new ArrayList<Object[]>();
		
		try {
			Connection conn = dataSource.getConnection();
			
			String query = "SELECT v.share_date, v.feelings, COUNT(id_vision) FROM vision AS v JOIN scenario AS s ON v.scenario = s.id_scenario WHERE v.scenario = ? AND s.lang = ? ";
			
			if(startDate != "") {
				query += "AND v.share_date > ?";
			}
			
			if(endDate != "") {
				query += "AND v.share_date < ?";
			}
			
			query += "GROUP BY v.share_date, v.feelings ORDER BY v.share_date ASC, v.feelings ASC";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setInt(1, idScenario);
			statement.setString(2, lang);
			
			int ps_count = 3;
			
			if(startDate != "") {
				statement.setDate(ps_count, Date.valueOf(startDate));
				ps_count++;
			}
			
			if(endDate != "") {
				statement.setDate(ps_count, Date.valueOf(endDate));
			}
			
			ResultSet resultSet = statement.executeQuery();
			
			ArrayList<Object> row = new ArrayList<Object>(Arrays.asList("", 0, 0, 0, 0, 0, 0, 0, 0));
			Object[] outputRow = new Object[row.size()];
			
			while(resultSet.next()) {
				if(!row.get(0).equals(resultSet.getString(1))) {
					if(!row.get(0).equals("")) {
						outputRow = row.toArray(outputRow);
		        		data.add(outputRow);
		        		
		        		row = new ArrayList<Object>(Arrays.asList("", 0, 0, 0, 0, 0, 0, 0, 0));
						outputRow = new Object[row.size()];
					}
		        	
					row.set(0, resultSet.getString(1));
				}
	        	
				row.set(resultSet.getInt(2), resultSet.getInt(3));
			}
			//collect last row
			outputRow = row.toArray(outputRow);
    		data.add(outputRow);
    		
			LocalDate startDateTmp = LocalDate.parse(startDate.substring(0, 10));

			while(!startDateTmp.equals(LocalDate.parse(endDate.substring(0, 10)))) {
				
				boolean found = false;
				
				for(int i = 0; i < data.size(); i++) {
					if(((String)data.get(i)[0]).equals(startDateTmp.toString())) {
						found = true;
						break;
					}
				}
				
				if(!found) {
					ArrayList<Object> new_row = new ArrayList<Object>(Arrays.asList(startDateTmp.toString(), 0, 0, 0, 0, 0, 0, 0, 0));
					outputRow = new_row.toArray();
	        		data.add(outputRow);
				}
				
				startDateTmp = startDateTmp.plusDays(1);
			}
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getMatchesPlayedOverTime", method = RequestMethod.GET)
	public List<Object[]> getMatchesPlayedOverTime(int idScenario, String startDate, String endDate) {

		String lang = "en";
		List<Object[]> data = new ArrayList<Object[]>();
		
		try {
			Connection conn = dataSource.getConnection();
			
			String query = "SELECT m.played_date, COUNT(id_vision) FROM (matchmaking AS m JOIN vision AS v ON m.vision_challenger = v.id_vision) JOIN scenario AS s ON v.scenario = s.id_scenario WHERE v.scenario = ? AND s.lang = ? ";
			
			if(startDate != "") {
				query += "AND m.played_date > ?";
			}
			
			if(endDate != "") {
				query += "AND m.played_date < ?";
			}
			
			query += "GROUP BY m.played_date ORDER BY m.played_date ASC";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setInt(1, idScenario);
			statement.setString(2, lang);
			
			int ps_count = 3;
			
			if(startDate != "") {
				statement.setDate(ps_count, Date.valueOf(startDate));
				ps_count++;
			}
			
			if(endDate != "") {
				statement.setDate(ps_count, Date.valueOf(endDate));
			}
			
			LocalDate startDateTmp = LocalDate.parse(startDate.substring(0, 10));
			
			ArrayList<ArrayList<Object>> output = new ArrayList<ArrayList<Object>>();
			ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
			while(!startDateTmp.equals(LocalDate.parse(endDate.substring(0, 10)))) {
				ArrayList<Object> row = new ArrayList<Object>(Arrays.asList(startDateTmp.toString(), 0));
				
				dates.add(startDateTmp);
				output.add(row);
				
				startDateTmp = startDateTmp.plusDays(1);
			}
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				output.set(dates.indexOf(LocalDate.parse(resultSet.getString(1).substring(0, 10))), new ArrayList<Object>(Arrays.asList(resultSet.getDate(1), resultSet.getInt(2))));
			}
			
			for(ArrayList<Object> row : output) {
				Object[] outputRow = new Object[row.size()];
				data.add(row.toArray(outputRow));
			}
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getQuickStats", method = RequestMethod.GET)
	public ArrayList<Integer> getQuickStats(int idScenario) {

		String lang = "en";
		ArrayList<Integer> data = new ArrayList<Integer>();
		
		try {
			Connection conn = dataSource.getConnection();
			
			String queryVisionCount = "SELECT COUNT(id_vision) FROM vision AS v JOIN scenario AS s ON v.scenario = s.id_scenario WHERE v.scenario = ? AND s.lang = ?";
			String queryMatchCount = "SELECT COUNT(id_match) FROM (matchmaking AS m JOIN vision AS v ON m.vision_challenger = v.id_vision) JOIN scenario AS s ON v.scenario = s.id_scenario WHERE v.scenario = ? AND s.lang = ?";
			String queryEmpathyPercentage = "SELECT COUNT(id_match) FROM (matchmaking AS m JOIN vision AS v ON m.vision_challenger = v.id_vision) JOIN scenario AS s ON v.scenario = s.id_scenario WHERE v.scenario = ? AND s.lang = ? AND m.guessed_feeling = v.feelings";
			
			PreparedStatement statementVisionCount = conn.prepareStatement(queryVisionCount);
			
			statementVisionCount.setInt(1, idScenario);
			statementVisionCount.setString(2, lang);
			
			ResultSet resultSetVisionCount = statementVisionCount.executeQuery();
			resultSetVisionCount.next();
			
			data.add(resultSetVisionCount.getInt(1));
			
			PreparedStatement statementMatchCount = conn.prepareStatement(queryMatchCount);
			
			statementMatchCount.setInt(1, idScenario);
			statementMatchCount.setString(2, lang);
			
			ResultSet resultSetMatchCount = statementMatchCount.executeQuery();
			resultSetMatchCount.next();
			
			data.add(resultSetMatchCount.getInt(1));
			
			PreparedStatement statementEmpathyPercentage = conn.prepareStatement(queryEmpathyPercentage);
			
			statementEmpathyPercentage.setInt(1, idScenario);
			statementEmpathyPercentage.setString(2, lang);
			
			ResultSet resultSetEmpathyPercentage = statementEmpathyPercentage.executeQuery();
			resultSetEmpathyPercentage.next();
			
			data.add(Math.round((float) resultSetEmpathyPercentage.getInt(1) / (float) resultSetMatchCount.getInt(1)));
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getEmpathyDistribution", method = RequestMethod.GET)
	public List<Object[]> getEmpathyDistribution(int idScenario) {

		String lang = "en";
		List<Object[]> data = new ArrayList<Object[]>();
		
		try {
			Connection conn = dataSource.getConnection();
			
			String queryCorrect = "SELECT u.cookie, COUNT(id_match) FROM ((matchmaking AS m JOIN vision AS v ON m.vision_challenger = v.id_vision) JOIN scenario AS s ON v.scenario = s.id_scenario) JOIN user AS u ON u.cookie = m.player WHERE v.scenario = ? AND s.lang = ? AND m.guessed_feeling = v.feelings GROUP BY u.cookie ORDER BY u.cookie";
			String queryPlayed = "SELECT u.cookie, COUNT(id_match) FROM ((matchmaking AS m JOIN vision AS v ON m.vision_challenger = v.id_vision) JOIN scenario AS s ON v.scenario = s.id_scenario) JOIN user AS u ON u.cookie = m.player WHERE v.scenario = ? AND s.lang = ? GROUP BY u.cookie ORDER BY u.cookie";
			
			PreparedStatement statementCorrect = conn.prepareStatement(queryCorrect);
			
			statementCorrect.setInt(1, idScenario);
			statementCorrect.setString(2, lang);
			
			ResultSet resultSetCorrect = statementCorrect.executeQuery();
			
			PreparedStatement statementPlayed = conn.prepareStatement(queryPlayed);
			
			statementPlayed.setInt(1, idScenario);
			statementPlayed.setString(2, lang);
			
			ResultSet resultSetPlayed = statementPlayed.executeQuery();
			
			ArrayList<ArrayList<Object>> output = new ArrayList<ArrayList<Object>>();
			ArrayList<String> users = new ArrayList<String>();
			while(resultSetPlayed.next()) {
				output.add(new ArrayList<Object>(Arrays.asList(resultSetPlayed.getString(1), resultSetPlayed.getInt(2), false)));
				users.add(resultSetPlayed.getString(1));
			}
			
			while(resultSetCorrect.next()) {
				int index = users.indexOf(resultSetCorrect.getString(1));
				
				output.set(index, new ArrayList<Object>(Arrays.asList(resultSetCorrect.getString(1), Math.round(((Integer) resultSetCorrect.getInt(2)).floatValue() / ((Integer) output.get(index).get(1)).floatValue()), true)));
			}		
			
			ArrayList<String> outputToCount = new ArrayList<String>();
			for(ArrayList<Object> row : output) {
				
				String out = row.get(1).toString();
				
				if(!(boolean) row.get(2)) {
					out = "0";
				}
				
				outputToCount.add(out);
			}
			
			ArrayList<ArrayList<Object>> outputCount = new ArrayList<ArrayList<Object>>();
			ArrayList<String> addedElements = new ArrayList<String>();
			
			for(int i = 0; i < 101; i++) {
				addedElements.add(i + "");
				outputCount.add(new ArrayList<Object>(Arrays.asList(i + "", 0)));
			}
			
			for(String el : outputToCount) {
				int index = addedElements.indexOf(el);
				
				if(index != -1) {
					outputCount.set(index, new ArrayList<Object>(Arrays.asList(el, (int) outputCount.get(index).get(1) + 1)));
				}
			}
			
			for(ArrayList<Object> row : outputCount) {
				Object[] outputRow = new Object[row.size()];
				data.add(row.toArray(outputRow));
			}
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public List<Object[]> getTopKeywords(int idScenario) {
		List<Object[]> data = new ArrayList<Object[]>();
		
		try {
			Connection conn = dataSource.getConnection();
			
			String query = "SELECT VK.id_keyword, K.Keyword, count(*) AS C "
					+ "FROM vision AS V JOIN vision_keyword AS VK ON V.id_vision = VK.id_vision "
					+ "JOIN keyword AS K ON K.id_keyword = VK.id_keyword "
					+ "WHERE V.scenario = ? "
					+ "GROUP BY VK.id_keyword ORDER BY C DESC";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setInt(1, idScenario);
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				ArrayList<String> row = new ArrayList<String>();
				String[] outputRow = new String[row.size()];
				
				row.add("" + resultSet.getInt(1));
				row.add(resultSet.getString(2));
				row.add("" + resultSet.getInt(3));
				
				outputRow = row.toArray(outputRow);
	        	data.add(outputRow);
			}
			
        	conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public List<Object[]> getDescriptionWords(int idScenario) throws IOException {
		List<Object[]> data = new ArrayList<Object[]>();
		
		List<String> stopwords = Files.readAllLines(Paths.get(Application.DATA + "/stopwords.txt"));
		
		try {
			Connection conn = dataSource.getConnection();
			
			String query = "SELECT V.description FROM vision AS V WHERE V.scenario = ?";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setInt(1, idScenario);
			
			ResultSet resultSet = statement.executeQuery();
			
			ArrayList<String> words = new ArrayList<String>();
			
			while(resultSet.next()) {
				ArrayList<String> allWords = Stream.of(resultSet.getString(1).toLowerCase().replaceAll("[^a-zA-Z ]", "").split("\\s+")).collect(Collectors.toCollection(ArrayList<String>::new));
				
				allWords.removeAll(stopwords);
				
				words.addAll(allWords);
			}
			
			ArrayList<String> wordString = new ArrayList<String>();
			ArrayList<Integer> wordCount = new ArrayList<Integer>();
			
			for(String w : words) {
				int index = wordString.indexOf(w);
				
				if(index == -1) {
					wordString.add(w);
					wordCount.add(1);
				} else {
					wordCount.set(index, wordCount.get(index) + 1);
				}
			}
			
			for(String w : wordString) {
				ArrayList<String> row = new ArrayList<String>();
				String[] outputRow = new String[row.size()];
				
				row.add(w);
				row.add("" + wordCount.get(wordString.indexOf(w)));
				
				outputRow = row.toArray(outputRow);
	        	data.add(outputRow);
			}
        	
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getSankeyFeelings", method = RequestMethod.GET)
	public List<Object[]> getSankeyFeelings(int idScenario) {

		String lang = "en";
		List<Object[]> data = new ArrayList<Object[]>();
		
		try {
			Connection conn = dataSource.getConnection();
			
			String query = "SELECT v.feelings, m.guessed_feeling, COUNT(*) AS 'count' FROM (matchmaking AS m JOIN vision AS v ON m.vision_challenger = v.id_vision) JOIN scenario AS s ON v.scenario = s.id_scenario WHERE v.scenario = ? AND s.lang = ? AND v.feelings != m.guessed_feeling GROUP BY v.feelings, m.guessed_feeling ORDER BY v.feelings ASC, m.guessed_feeling ASC";
			
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, idScenario);
			statement.setString(2, lang);
			
			ResultSet resultSet = statement.executeQuery();
			
			ArrayList<ArrayList<Object>> sankeyData = new ArrayList<ArrayList<Object>>();
			for(int i = 1; i < 9; i++) {
				for(int j = i + 1; j < 9; j++) {
					ArrayList<Object> row = new ArrayList<Object>();
					
					row.add(sentimentMapper(i));
					row.add(sentimentMapper(j));
					row.add(0);
					
					sankeyData.add(row);
				}
			}
			
			while(resultSet.next()) {
				if(resultSet.getInt(2) != 0 && !sentimentMapper(resultSet.getInt(1)).equals(sentimentMapper(resultSet.getInt(2)))) {
					for(ArrayList<Object> couple : sankeyData) {
						if((couple.get(0).toString().equals(sentimentMapper(resultSet.getInt(1))) && couple.get(1).toString().equals(sentimentMapper(resultSet.getInt(2))))
								|| (couple.get(0).toString().equals(sentimentMapper(resultSet.getInt(2))) && couple.get(1).toString().equals(sentimentMapper(resultSet.getInt(1))))) {
							int index = sankeyData.indexOf(couple);
							
							sankeyData.set(index, new ArrayList<Object>(Arrays.asList(sankeyData.get(index).get(0).toString(), sankeyData.get(index).get(1).toString(), (int) sankeyData.get(index).get(2) + resultSet.getInt(3))));
						}
					}
				}
			}
			
			for(ArrayList<Object> row : sankeyData) {
				if((int) row.get(2) != 0) {
					Object[] outputRow = new Object[row.size()];
					outputRow = row.toArray(outputRow);
					data.add(outputRow);
				}
			}
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getMatchesNetwork", method = RequestMethod.GET)
	public String getMatchesNetwork(int idScenario) {
		
		String lang = "en";
		JSONObject graph = new JSONObject();
		
		try {
			Connection conn = dataSource.getConnection();
			
			String query = "SELECT m.player, v.cookie, COUNT(id_match) FROM (matchmaking AS m JOIN vision AS v ON m.vision_challenger = v.id_vision) JOIN scenario AS s ON v.scenario = s.id_scenario WHERE v.scenario = ? AND s.lang = ? GROUP BY m.player, v.cookie";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setInt(1, idScenario);
			statement.setString(2, lang);
			
			ResultSet resultSet = statement.executeQuery();
			
			JSONArray nodes = new JSONArray();
			JSONArray links = new JSONArray();
			
			ArrayList<String> added = new ArrayList<String>();
			while(resultSet.next()) {
				
				if(added.indexOf(resultSet.getString(1)) == -1) {
					JSONObject node = new JSONObject();
					
					node.put("id", resultSet.getString(1));
					
					added.add(resultSet.getString(1));
					
					nodes.put(node);
				}
				
				if(added.indexOf(resultSet.getString(2)) == -1) {
					JSONObject node = new JSONObject();
					
					node.put("id", resultSet.getString(2));
					
					added.add(resultSet.getString(2));
					
					nodes.put(node);
				}
				
				JSONObject link = new JSONObject();
				
				link.put("source", resultSet.getString(1));
				link.put("target", resultSet.getString(2));
				link.put("value", resultSet.getInt(3));
				
				links.put(link);
			}
			
			graph.put("nodes", nodes);
			graph.put("links", links);
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return graph.toString();
	}
}
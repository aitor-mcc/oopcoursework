package uk.ac.ucl.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Model
{
  DataLoader dataLoader;
  DataFrame data;
  private static final String FIRST_NAME = "FIRST";
  private static final String LAST_NAME = "LAST";
  private static final String ID = "ID";
  private static final String BIRTHDATE = "BIRTHDATE";
  private static final String CITY = "CITY";
  private static final String STATE = "STATE";
  private static final String ETHNICITY = "ETHNICITY";
  private static final String RACE = "RACE";
  private static final String GENDER = "GENDER";

  //constructor
  public Model(String filename) throws IOException {
    this.dataLoader = new DataLoader(filename);
    this.data = this.dataLoader.getData();
  }

  public List<Patient> getPatients()
  {
    return getPatients("name-asc");
  }

  public List<Patient> getPatients(String sortBy)
  {
    ArrayList<Integer> rowIndexes = new ArrayList<>();

    for (int i = 0; i < this.data.getRowCount(); i++) {
      rowIndexes.add(i);
    }

    Comparator<Integer> comparator;

    switch (sortBy) {
      case "name-desc":
        comparator = Comparator.comparing(
                this::getPatientName,
                String.CASE_INSENSITIVE_ORDER
        ).reversed();
        break;
      case "age-asc":
        comparator = Comparator.comparing(this::getBirthDateSafe).reversed();
        break;
      case "age-desc":
        comparator = Comparator.comparing(this::getBirthDateSafe);
        break;
      case "name-asc":
      default:
        comparator = Comparator.comparing(
                this::getPatientName,
                String.CASE_INSENSITIVE_ORDER
        );
        break;
    }

    rowIndexes.sort(comparator);

    ArrayList<Patient> patients = new ArrayList<>();
    for (Integer row : rowIndexes) {
      patients.add(new Patient(this.data.getValue(ID, row), getPatientName(row)));
    }

    return patients;
  }

  private String getPatientName(int row)
  {
    String first = this.data.getValue(FIRST_NAME, row);
    String last = this.data.getValue(LAST_NAME, row);
    return first + " " + last;
  }

  private String getBirthDateSafe(int row)
  {
    String birthDate = this.data.getValue(BIRTHDATE, row);
    return birthDate == null ? "" : birthDate;
  }

  public Map<String, String> getPatientData(String id) {
    Map<String, String> patientData = new LinkedHashMap<>();
    ArrayList<String> columns = this.data.getColumnNames();

    for (int i = 0; i < this.data.getRowCount(); i++) {
      if (this.data.getValue(ID, i).equals(id)) {
        for (String columnName : columns) {
          patientData.put(columnName, this.data.getValue(columnName, i));
        }
        return patientData;
      }
    }

    return null;
  }

  // This also returns dummy data. The real version should use the keyword parameter to search
  // the data and return a list of matching items.
  // Used by SearchServlet
  public List<Patient> searchFor(String keyword) {
    List<Patient> results = new ArrayList<>();
    List<String> columnNames = data.getColumnNames();
    int rowCount = data.getRowCount();
    String lowerKeyword = keyword.toLowerCase();
    String first;
    String last;

    for (int row = 0; row < rowCount; row++) {
      for (String columnName : columnNames) {
        String value = data.getValue(columnName, row);
        if (value != null && value.toLowerCase().contains(lowerKeyword)) {
          first = this.data.getValue(FIRST_NAME,row);
          last = this.data.getValue(LAST_NAME,row);
          Patient patient = new Patient(this.data.getValue(ID,row), first + " " + last);
          results.add(patient);
          break;
        }
      }
    }
    return results;
  }

  /**
   * Gets a sorted list of all unique cities where patients live.
   * @return List of city names sorted alphabetically
   */
  public List<String> getCities() {
    Set<String> citySet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    int rowCount = data.getRowCount();

    for (int row = 0; row < rowCount; row++) {
      String city = data.getValue(CITY, row);
      if (city != null && !city.trim().isEmpty()) {
        citySet.add(city.trim());
      }
    }

    return new ArrayList<>(citySet);
  }

  /**
   * Gets a map of cities with their resident counts, sorted alphabetically by city name.
   * @return Map of city names to resident counts
   */
  public Map<String, Integer> getCitiesWithCounts() {
    Map<String, Integer> cityCounts = new HashMap<>();
    int rowCount = data.getRowCount();

    for (int row = 0; row < rowCount; row++) {
      String city = data.getValue(CITY, row);
      if (city != null && !city.trim().isEmpty()) {
        String trimmedCity = city.trim();
        cityCounts.put(trimmedCity, cityCounts.getOrDefault(trimmedCity, 0) + 1);
      }
    }

    // Convert to TreeMap for alphabetical sorting
    return new TreeMap<>(String.CASE_INSENSITIVE_ORDER) {{
      putAll(cityCounts);
    }};
  }

  /**
   * Gets all patients from a specific city, sorted by the specified criteria.
   * @param city the city to filter by
   * @param sortBy the sort criteria (name-asc, name-desc, age-asc, age-desc)
   * @return List of patients from the specified city
   */
  public List<Patient> getPatientsByCity(String city, String sortBy) {
    ArrayList<Integer> rowIndexes = new ArrayList<>();

    // Find all rows matching the city
    for (int i = 0; i < this.data.getRowCount(); i++) {
      String patientCity = this.data.getValue(CITY, i);
      if (patientCity != null && patientCity.trim().equalsIgnoreCase(city.trim())) {
        rowIndexes.add(i);
      }
    }

    // Sort using the same comparator logic as getPatients
    Comparator<Integer> comparator;
    switch (sortBy) {
      case "name-desc":
        comparator = Comparator.comparing(
                this::getPatientName,
                String.CASE_INSENSITIVE_ORDER
        ).reversed();
        break;
      case "age-asc":
        comparator = Comparator.comparing(this::getBirthDateSafe).reversed();
        break;
      case "age-desc":
        comparator = Comparator.comparing(this::getBirthDateSafe);
        break;
      case "name-asc":
      default:
        comparator = Comparator.comparing(
                this::getPatientName,
                String.CASE_INSENSITIVE_ORDER
        );
        break;
    }

    rowIndexes.sort(comparator);

    ArrayList<Patient> patients = new ArrayList<>();
    for (Integer row : rowIndexes) {
      patients.add(new Patient(this.data.getValue(ID, row), getPatientName(row)));
    }

    return patients;
  }

  /**
   * Gets a map of states with their resident counts, sorted alphabetically by state name.
   * @return Map of state names to resident counts
   */
  public Map<String, Integer> getStatesWithCounts() {
    Map<String, Integer> stateCounts = new HashMap<>();
    int rowCount = data.getRowCount();

    for (int row = 0; row < rowCount; row++) {
      String state = data.getValue(STATE, row);
      if (state != null && !state.trim().isEmpty()) {
        String trimmedState = state.trim();
        stateCounts.put(trimmedState, stateCounts.getOrDefault(trimmedState, 0) + 1);
      }
    }

    // Convert to TreeMap for alphabetical sorting
    return new TreeMap<>(String.CASE_INSENSITIVE_ORDER) {{
      putAll(stateCounts);
    }};
  }

  /**
   * Gets all patients from a specific state, sorted by the specified criteria.
   * @param state the state to filter by
   * @param sortBy the sort criteria (name-asc, name-desc, age-asc, age-desc)
   * @return List of patients from the specified state
   */
  public List<Patient> getPatientsByState(String state, String sortBy) {
    ArrayList<Integer> rowIndexes = new ArrayList<>();

    // Find all rows matching the state
    for (int i = 0; i < this.data.getRowCount(); i++) {
      String patientState = this.data.getValue(STATE, i);
      if (patientState != null && patientState.trim().equalsIgnoreCase(state.trim())) {
        rowIndexes.add(i);
      }
    }

    // Sort using the same comparator logic as getPatients
    Comparator<Integer> comparator;
    switch (sortBy) {
      case "name-desc":
        comparator = Comparator.comparing(
                this::getPatientName,
                String.CASE_INSENSITIVE_ORDER
        ).reversed();
        break;
      case "age-asc":
        comparator = Comparator.comparing(this::getBirthDateSafe).reversed();
        break;
      case "age-desc":
        comparator = Comparator.comparing(this::getBirthDateSafe);
        break;
      case "name-asc":
      default:
        comparator = Comparator.comparing(
                this::getPatientName,
                String.CASE_INSENSITIVE_ORDER
        );
        break;
    }

    rowIndexes.sort(comparator);

    ArrayList<Patient> patients = new ArrayList<>();
    for (Integer row : rowIndexes) {
      patients.add(new Patient(this.data.getValue(ID, row), getPatientName(row)));
    }

    return patients;
  }

  /**
   * Gets a map of ethnicities with their counts, sorted alphabetically.
   * @return Map of ethnicity names to counts
   */
  public Map<String, Integer> getEthnicitiesWithCounts() {
    Map<String, Integer> ethnicityCounts = new HashMap<>();
    int rowCount = data.getRowCount();

    for (int row = 0; row < rowCount; row++) {
      String ethnicity = data.getValue(ETHNICITY, row);
      if (ethnicity != null && !ethnicity.trim().isEmpty()) {
        String trimmedEthnicity = ethnicity.trim();
        ethnicityCounts.put(trimmedEthnicity, ethnicityCounts.getOrDefault(trimmedEthnicity, 0) + 1);
      }
    }

    return new TreeMap<>(String.CASE_INSENSITIVE_ORDER) {{
      putAll(ethnicityCounts);
    }};
  }

  /**
   * Gets all patients of a specific ethnicity, sorted by the specified criteria.
   * @param ethnicity the ethnicity to filter by
   * @param sortBy the sort criteria (name-asc, name-desc, age-asc, age-desc)
   * @return List of patients of the specified ethnicity
   */
  public List<Patient> getPatientsByEthnicity(String ethnicity, String sortBy) {
    return getPatientsByAttribute(ETHNICITY, ethnicity, sortBy);
  }

  /**
   * Gets a map of races with their counts, sorted alphabetically.
   * @return Map of race names to counts
   */
  public Map<String, Integer> getRacesWithCounts() {
    Map<String, Integer> raceCounts = new HashMap<>();
    int rowCount = data.getRowCount();

    for (int row = 0; row < rowCount; row++) {
      String race = data.getValue(RACE, row);
      if (race != null && !race.trim().isEmpty()) {
        String trimmedRace = race.trim();
        raceCounts.put(trimmedRace, raceCounts.getOrDefault(trimmedRace, 0) + 1);
      }
    }

    return new TreeMap<>(String.CASE_INSENSITIVE_ORDER) {{
      putAll(raceCounts);
    }};
  }

  /**
   * Gets all patients of a specific race, sorted by the specified criteria.
   * @param race the race to filter by
   * @param sortBy the sort criteria (name-asc, name-desc, age-asc, age-desc)
   * @return List of patients of the specified race
   */
  public List<Patient> getPatientsByRace(String race, String sortBy) {
    return getPatientsByAttribute(RACE, race, sortBy);
  }

  /**
   * Gets a map of genders with their counts, sorted alphabetically.
   * @return Map of gender to counts
   */
  public Map<String, Integer> getGendersWithCounts() {
    Map<String, Integer> genderCounts = new HashMap<>();
    int rowCount = data.getRowCount();

    for (int row = 0; row < rowCount; row++) {
      String gender = data.getValue(GENDER, row);
      if (gender != null && !gender.trim().isEmpty()) {
        String trimmedGender = gender.trim();
        genderCounts.put(trimmedGender, genderCounts.getOrDefault(trimmedGender, 0) + 1);
      }
    }

    return new TreeMap<>(String.CASE_INSENSITIVE_ORDER) {{
      putAll(genderCounts);
    }};
  }

  /**
   * Gets all patients of a specific gender, sorted by the specified criteria.
   * @param gender the gender to filter by
   * @param sortBy the sort criteria (name-asc, name-desc, age-asc, age-desc)
   * @return List of patients of the specified gender
   */
  public List<Patient> getPatientsByGender(String gender, String sortBy) {
    return getPatientsByAttribute(GENDER, gender, sortBy);
  }

  /**
   * Helper method to get patients filtered by any attribute.
   * @param attributeName the column name to filter by
   * @param attributeValue the value to filter by
   * @param sortBy the sort criteria
   * @return List of patients matching the attribute
   */
  private List<Patient> getPatientsByAttribute(String attributeName, String attributeValue, String sortBy) {
    ArrayList<Integer> rowIndexes = new ArrayList<>();

    for (int i = 0; i < this.data.getRowCount(); i++) {
      String value = this.data.getValue(attributeName, i);
      if (value != null && value.trim().equalsIgnoreCase(attributeValue.trim())) {
        rowIndexes.add(i);
      }
    }

    Comparator<Integer> comparator;
    switch (sortBy) {
      case "name-desc":
        comparator = Comparator.comparing(
                this::getPatientName,
                String.CASE_INSENSITIVE_ORDER
        ).reversed();
        break;
      case "age-asc":
        comparator = Comparator.comparing(this::getBirthDateSafe).reversed();
        break;
      case "age-desc":
        comparator = Comparator.comparing(this::getBirthDateSafe);
        break;
      case "name-asc":
      default:
        comparator = Comparator.comparing(
                this::getPatientName,
                String.CASE_INSENSITIVE_ORDER
        );
        break;
    }

    rowIndexes.sort(comparator);

    ArrayList<Patient> patients = new ArrayList<>();
    for (Integer row : rowIndexes) {
      patients.add(new Patient(this.data.getValue(ID, row), getPatientName(row)));
    }

    return patients;
  }

  public ArrayList<String> getColumnNames() {
    return new ArrayList<>(data.getColumnNames());
  }

  /**
   * Gets the underlying DataFrame.
   * @return the DataFrame containing all patient data
   */
  public DataFrame getDataFrame() {
    return data;
  }

  //methods for editing data
  /**
   * Adds a new patient row to the DataFrame.
   * @param rowData map of column name → value for every column
   */
  public void addPatient(Map<String, String> rowData) {
    for (String columnName : data.getColumnNames()) {
      data.addValue(columnName, rowData.getOrDefault(columnName, ""));
    }
  }

  /**
   * Updates an existing patient row identified by ID.
   * @param id      the patient ID to find
   * @param rowData map of column name → new value
   */
  public void updatePatient(String id, Map<String, String> rowData) {
    for (int i = 0; i < data.getRowCount(); i++) {
      if (data.getValue(ID, i).equals(id)) {
        for (String columnName : data.getColumnNames()) {
          if (rowData.containsKey(columnName)) {
            data.putValue(columnName, i, rowData.get(columnName));
          }
        }
        return;
      }
    }
  }

  /**
   * Deletes the patient row with the given ID.
   * @param id the patient ID to delete
   */
  public void deletePatient(String id) {
    for (int i = 0; i < data.getRowCount(); i++) {
      if (data.getValue(ID, i).equals(id)) {
        data.removeRow(i);
        return;
      }
    }
  }

  /**
   * Writes the current DataFrame back to the CSV file.
   * @throws IOException if writing fails
   */
  public void saveToCSV() throws IOException {
    List<String> columns = data.getColumnNames();
    String filename = dataLoader.getFilename();
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
      // Write header
      writer.write(String.join(",", columns));
      writer.newLine();
      // Write each row
      for (int row = 0; row < data.getRowCount(); row++) {
        StringBuilder sb = new StringBuilder();
        for (int col = 0; col < columns.size(); col++) {
          if (col > 0) sb.append(",");
          String val = data.getValue(columns.get(col), row);
          sb.append(val == null ? "" : val);
        }
        writer.write(sb.toString());
        writer.newLine();
      }
    }
  }

/**
 * Returns a map of age brackets to patient counts.
 * Brackets: 0-9, 10-19, 20-29, ... 90+
 */
public Map<String, Integer> getAgeDistribution() {
    Map<String, Integer> buckets = new LinkedHashMap<>();
    // Initialise buckets in order
    for (int i = 0; i <= 90; i += 10) {
        buckets.put(i + "-" + (i + 9), 0);
    }

    int currentYear = java.time.LocalDate.now().getYear();

    for (int row = 0; row < data.getRowCount(); row++) {
        String birthDate = data.getValue(BIRTHDATE, row);
        if (birthDate != null && birthDate.length() >= 4) {
            try {
                int birthYear = Integer.parseInt(birthDate.substring(0, 4));
                int age = currentYear - birthYear;
                int bucketStart = Math.min((age / 10) * 10, 90);
                String key = bucketStart + "-" + (bucketStart + 9);
                buckets.put(key, buckets.getOrDefault(key, 0) + 1);
            } catch (NumberFormatException ignored) {}
        }
    }
    return buckets;
}
}
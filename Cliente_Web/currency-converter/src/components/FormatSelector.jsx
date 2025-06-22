const FormatSelector = ({ formato, setFormato }) => (
  <div className="form-group">
    <label>Formato de resposta:</label>
    <select value={formato} onChange={(e) => setFormato(e.target.value)}>
      <option value="json">JSON</option>
      <option value="xml">XML</option>
    </select>
  </div>
);

export default FormatSelector;

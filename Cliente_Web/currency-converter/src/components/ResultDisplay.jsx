const ResultDisplay = ({ result }) => (
  result ? (
    <div className="result">
      <h3>Resultado</h3>
      <p>{result.originalAmount} {result.from} = {result.convertedAmount} {result.to}</p>
    </div>
  ) : null
);

export default ResultDisplay;

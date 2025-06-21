export function parseXmlHistory(xmlObj) {
  if (!xmlObj || !xmlObj.List) return [];

  let list = xmlObj.List.item;

  // Garante que seja um array mesmo com 1 item só
  if (!Array.isArray(list)) {
    list = [list];
  }

  return list.map(item => ({
    id: Number(item.id),
    fromCurrency: item.fromCurrency,
    toCurrency: item.toCurrency,
    amount: Number(item.amount),
    convertedAmount: Number(item.convertedAmount),
    timestamp: new Date(item.timestamp).toISOString(),
  }));
}

export function parseXmlConvertResponse(xmlObj) {
  const resp = xmlObj.ConvertCurrencyResponseDTO;
  return {
    from: resp.from,
    to: resp.to,
    originalAmount: Number(resp.originalAmount),
    convertedAmount: Number(resp.convertedAmount),
  };
}
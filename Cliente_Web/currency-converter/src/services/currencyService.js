import axios from 'axios';

// Importa o parser XML (para converter XML em JSON)
import { XMLParser } from 'fast-xml-parser';

// Importa funções auxiliares para tratar as respostas XML
import { parseXmlConvertResponse, parseXmlHistory } from './xmlParsers';

// Define a URL base da API (back-end)
const API_BASE = 'http://localhost:9080/v1/currency';

// Instancia um parser XML reutilizável
const xmlParser = new XMLParser();

/**
 * Função para buscar o histórico de conversões.
 * Retorna os dados em formato JSON ou XML, dependendo do parâmetro `formato`.
 */
export const fetchHistory = async (formato = 'json') => {
  const response = await axios.get(`${API_BASE}/history?formato=${formato}`, {
    headers: { Accept: formato === 'xml' ? 'application/xml' : 'application/json' },
    responseType: formato === 'xml' ? 'text' : 'json', // necessário para tratar XML como texto
  });

  // Se for XML, converte para objeto JavaScript usando o parser
  if (formato === 'xml') {
    const jsonObj = xmlParser.parse(response.data);
    const parsed = parseXmlHistory(jsonObj); // Normaliza a estrutura do XML
    return parsed;
  }

  return response.data; // JSON puro
};

/**
 * Função que envia uma requisição de conversão de moedas.
 * O back-end retorna os valores convertidos, em JSON ou XML.
 */
export const convertCurrency = async (from, to, amount, formato = 'json') => {
  const response = await axios.post(
    `${API_BASE}/convert?formato=${formato}`,
    { from, to, amount }, // corpo da requisição POST
    {
      headers: { Accept: formato === 'xml' ? 'application/xml' : 'application/json' },
      responseType: formato === 'xml' ? 'text' : 'json',
    }
  );

  // Se XML, faz o parse; senão retorna JSON diretamente
  return formato === 'xml'
    ? parseXmlConvertResponse(xmlParser.parse(response.data))
    : response.data;
};

/**
 * Função para excluir uma entrada do histórico (via ID).
 * Usa o método DELETE da API.
 */
export const deleteExchange = async (id, formato = 'json') => {
  await axios.delete(`${API_BASE}/delete-exchange?formato=${formato}`, {
    params: { idExchange: id },
    headers: { Accept: formato === 'xml' ? 'application/xml' : 'application/json' },
    responseType: formato === 'xml' ? 'text' : 'json',
  });
};

/**
 * Atualiza o campo `timestamp` de um item no histórico com novo valor.
 */
export const updateTimestamp = async (id, newTimestamp, formato = 'json') => {
  await axios.put(
    `${API_BASE}/update-timestamp?id=${id}&newTimestamp=${newTimestamp}&formato=${formato}`,
    null, // Sem corpo na requisição PUT
    {
      headers: { Accept: formato === 'xml' ? 'application/xml' : 'application/json' },
      responseType: formato === 'xml' ? 'text' : 'json',
    }
  );
};

/**
 * Função para download do histórico no formato JSON.
 * O `responseType: 'blob'` indica que o retorno será um arquivo.
 */
export const downloadJson = () =>
  axios.get(`${API_BASE}/download`, {
    responseType: 'blob',
    headers: { Accept: 'application/json' },
  });

/**
 * Função para download do histórico no formato XML.
 * Também retorna um arquivo para download.
 */
export const downloadXml = () =>
  axios.get(`${API_BASE}/download`, {
    responseType: 'blob',
    headers: { Accept: 'application/xml' },
  });

/**
 * Download do histórico em formato binário Protobuf (.pb)
 */
export const downloadProtobuf = () =>
  axios.get(`${API_BASE}/download-protobuf`, {
    responseType: 'blob',
    headers: { Accept: 'application/x-protobuf' },
  });

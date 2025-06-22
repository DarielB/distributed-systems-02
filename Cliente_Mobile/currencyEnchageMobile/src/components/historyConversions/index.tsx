import React, { useState } from 'react';
import { ScrollView, TextInput, Text, View, TouchableOpacity, StyleSheet } from 'react-native';

const HistoricoConversoes = (props: any) => {
    const { historico, deletar, salvarTimestamp } = props;
    const [editId, setEditId] = useState(null);
    const [editTimestamp, setEditTimestamp] = useState('');

    const handleEditClick = (entry: any) => {
        setEditId(entry.id);
        // Converte o timestamp para string compatível com input datetime-local
        const dt = new Date(entry.timestamp);
        const isoString = dt.toISOString().slice(0, 16); // "YYYY-MM-DDTHH:mm"
        setEditTimestamp(isoString);
    };

    const handleSaveClick = () => {
        salvarTimestamp(editId, editTimestamp);
        setEditId(null);
    };

    const handleCancelClick = () => {
        setEditId(null);
    };

    const formatarData = (dataString: string) => {
        const data = new Date(dataString);
        const dia = String(data.getDate()).padStart(2, '0');
        const mes = String(data.getMonth() + 1).padStart(2, '0');
        const ano = String(data.getFullYear()).slice(2); // pega os dois últimos dígitos
        const horas = String(data.getHours()).padStart(2, '0');
        const minutos = String(data.getMinutes()).padStart(2, '0');

        return `${dia}/${mes}/${ano} ${horas}:${minutos}`;
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Histórico de Conversões</Text>

            <View style={styles.tableHeader}>
                <Text style={[styles.headerCell, styles.smallColumn]}>De</Text>
                <Text style={[styles.headerCell, styles.smallColumn]}>Para</Text>
                <Text style={styles.headerCell}>Valor</Text>
                <Text style={styles.headerCell}>Convertido</Text>
                <Text style={styles.headerCell}>Data/Hora</Text>
                <Text style={styles.headerCell}>Ações</Text>
            </View>

            {historico.map((entry: any) => (
                <View key={entry.id} style={styles.tableRow}>
                    <Text style={[styles.cell, styles.smallColumn]}>{entry.fromCurrency}</Text>
                    <Text style={[styles.cell, styles.smallColumn]}>{entry.toCurrency}</Text>
                    <Text style={styles.cell}>{entry.amount}</Text>
                    <Text style={styles.cell}>{entry.convertedAmount}</Text>
                    <View style={styles.cell}>
                        {editId === entry.id ? (
                            <TextInput
                                style={styles.input}
                                value={editTimestamp}
                                onChangeText={setEditTimestamp}
                                placeholder="YYYY-MM-DD HH:MM"
                            />
                        ) : (
                            <Text>{formatarData(entry.timestamp)}</Text>
                        )}
                    </View>

                    <View style={styles.cell}>
                        {editId === entry.id ? (
                            <View style={styles.buttonGroup}>
                                <TouchableOpacity style={styles.button} onPress={handleSaveClick}>
                                    <Text style={styles.buttonText}>Salvar</Text>
                                </TouchableOpacity>
                                <TouchableOpacity style={styles.button} onPress={handleCancelClick}>
                                    <Text style={styles.buttonText}>Cancelar</Text>
                                </TouchableOpacity>
                            </View>
                        ) : (
                            <View style={styles.buttonGroup}>
                                <TouchableOpacity style={styles.button} onPress={() => handleEditClick(entry)}>
                                    <Text style={styles.buttonText}>Editar</Text>
                                </TouchableOpacity>
                                <TouchableOpacity style={styles.button} onPress={() => deletar(entry.id)}>
                                    <Text style={styles.buttonText}>Excluir</Text>
                                </TouchableOpacity>
                            </View>
                        )}
                    </View>
                </View>
            ))}
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        padding: 10,
        marginTop: 20,
        marginBottom: 50,
        backgroundColor: '#fff'
    },
    title: {
        fontSize: 20,
        fontWeight: 'bold',
        marginBottom: 10,
    },
    tableHeader: {
        flexDirection: 'row',
        backgroundColor: '#4472c4',
        padding: 5,
    },
    headerCell: {
        flex: 1.2, // padrão maior
        color: '#fff',
        fontWeight: 'bold',
        textAlign: 'center',
        fontSize: 12,
    },
    smallColumn: {
        flex: 0.7, // reduzido para "De" e "Para"
    },
    tableRow: {
        flexDirection: 'row',
        borderBottomWidth: 1,
        borderColor: '#ccc',
        paddingVertical: 5,
    },
    cell: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        textAlign: 'center',
        paddingHorizontal: 2,
    },
    input: {
        borderWidth: 1,
        borderColor: '#aaa',
        borderRadius: 4,
        padding: 4,
        width: '100%',
        fontSize: 12,
    },
    buttonGroup: {
        flexDirection: 'column', // agora os botões ficam na vertical
        alignItems: 'center', // opcional, para centralizar os botões
        gap: 4, // se quiser espaço entre eles (funciona no React Native mais novo)
    },
    button: {
        backgroundColor: '#4472c4',
        paddingHorizontal: 6,
        paddingVertical: 4,
        borderRadius: 4,
        marginHorizontal: 2,
    },
    buttonText: {
        color: '#fff',
        fontSize: 12,
    },
});

export default HistoricoConversoes;

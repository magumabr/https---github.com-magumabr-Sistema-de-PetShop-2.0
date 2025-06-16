import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PetshopSystemSwing {
    // Classes para estruturar dados
    static class Cliente implements Serializable {
        private static final long serialVersionUID = 2L;
        String nome;
        String email;
        String telefone;
        String cpf;
        String cep;
        String sexo;

        Cliente(String nome, String email, String telefone, String cpf, String cep, String sexo) {
            this.nome = nome;
            this.email = email != null ? email : "";
            this.telefone = telefone != null ? telefone : "";
            this.cpf = cpf != null ? cpf : "";
            this.cep = cep != null ? cep : "";
            this.sexo = sexo;
        }

        public String toString() {
            return nome + (cpf.isEmpty() ? "" : " (CPF: " + cpf + ")");
        }
    }

    static class Animal implements Serializable {
        private static final long serialVersionUID = 3L;
        String nome;
        ArrayList<Cliente> clientes;
        boolean semDono;
        boolean paraAdocao;
        String observacao;
        String raca;
        String sexo;

        Animal(String nome, boolean semDono, String observacao, String raca, String sexo) {
            this.nome = nome;
            this.clientes = new ArrayList<>();
            this.semDono = semDono;
            this.paraAdocao = semDono;
            this.observacao = observacao != null ? observacao : "";
            this.raca = raca;
            this.sexo = sexo != null ? sexo : "Macho";
        }

        void adicionarCliente(Cliente cliente) {
            if (!clientes.contains(cliente)) {
                clientes.add(cliente);
                semDono = false;
                paraAdocao = false;
            }
        }

        void removerCliente(Cliente cliente) {
            clientes.remove(cliente);
            if (clientes.isEmpty()) {
                semDono = true;
                paraAdocao = true;
            }
        }

        public String getDonos() {
            return clientes.isEmpty() ? "Sem dono (Para adoção)" : String.join(", ", clientes.stream().map(Cliente::toString).collect(Collectors.toList()));
        }

        public String toString() {
            return nome + " (" + getDonos() + ")";
        }
    }

    static class Agendamento implements Serializable {
        private static final long serialVersionUID = 1L;
        Animal animal;
        Date horario;
        String detalhes;

        Agendamento(Animal animal, Date horario, String detalhes) {
            this.animal = animal;
            this.horario = horario;
            this.detalhes = detalhes != null ? detalhes : "";
        }

        public String getDataFormatada() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(horario);
        }

        public String getHorarioFormatado() {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(horario);
        }

        public String toString() {
            return animal.toString() + " - " + getDataFormatada() + " " + getHorarioFormatado() + " - " + detalhes;
        }
    }

    // Listas para armazenar dados
    static ArrayList<Cliente> clientes = new ArrayList<>();
    static ArrayList<Animal> animais = new ArrayList<>();
    static ArrayList<Agendamento> agendamentos = new ArrayList<>();
    static ArrayList<String> produtos = new ArrayList<>();
    static ArrayList<String> vendas = new ArrayList<>();
    static ArrayList<Funcionario> funcionarios = new ArrayList<>();
    static ArrayList<String> relatorios = new ArrayList<>();

    // Arquivo para persistência
    private static final String ARQUIVO_DADOS = "petshop.dat";

    // Métodos para persistência
    private static void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(clientes);
            oos.writeObject(animais);
            oos.writeObject(agendamentos);
            oos.writeObject(funcionarios);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private static void carregarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            clientes = (ArrayList<Cliente>) ois.readObject();
            animais = (ArrayList<Animal>) ois.readObject();
            agendamentos = (ArrayList<Agendamento>) ois.readObject();
            funcionarios = (ArrayList<Funcionario>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Arquivo não existe ainda, inicializar listas vazias
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            carregarDados();
            JFrame mainFrame = new JFrame("Sistema para Petshop");
            mainFrame.setSize(600, 400);
            mainFrame.setLayout(new GridLayout(4, 2));
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton btnCadastro = new JButton("Cadastro");
            JButton btnClientes = new JButton("Clientes");
            JButton btnAnimais = new JButton("Animais");
            JButton btnAgendamentos = new JButton("Agendamentos");
            JButton btnProdutos = new JButton("Produtos");
            JButton btnVendas = new JButton("Vendas");
            JButton btnFuncionarios = new JButton("Funcionários");
            JButton btnRelatorios = new JButton("Relatórios");

            mainFrame.add(btnCadastro);
            mainFrame.add(btnClientes);
            mainFrame.add(btnAnimais);
            mainFrame.add(btnAgendamentos);
            mainFrame.add(btnProdutos);
            mainFrame.add(btnVendas);
            mainFrame.add(btnFuncionarios);
            mainFrame.add(btnRelatorios);

            btnCadastro.addActionListener(e -> abrirTelaCadastro());
            btnClientes.addActionListener(e -> abrirTelaClientes());
            btnAnimais.addActionListener(e -> abrirTelaAnimais());
            btnAgendamentos.addActionListener(e -> abrirTelaAgendamentos());
            btnProdutos.addActionListener(e -> abrirTelaProdutos());
            btnVendas.addActionListener(e -> abrirTelaVendas());
            btnFuncionarios.addActionListener(e -> abrirTelaFuncionarios());
            btnRelatorios.addActionListener(e -> abrirTelaRelatorios());

            mainFrame.setLocationRelativeTo(null);
            mainFrame.setVisible(true);
        });
    }

    static class Funcionario implements Serializable {
    private static final long serialVersionUID = 1L;
    String nome;
    String email;
    String telefone;
    String cpf;
    String cep;
    String sexo;

    Funcionario(String nome, String email, String telefone, String cpf, String cep, String sexo) {
        this.nome = nome;
        this.email = email != null ? email : "";
        this.telefone = telefone != null ? telefone : "";
        this.cpf = cpf != null ? cpf : "";
        this.cep = cep != null ? cep : "";
        this.sexo = sexo;
    }

    public String toString() {
        return nome + (cpf.isEmpty() ? "" : " (CPF: " + cpf + ")");
    }
}

    private static void abrirTelaClientes() {
        JFrame frame = new JFrame("Gerenciar Clientes");
        frame.setSize(700, 400);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField txtBusca = new JTextField(20);
        String[] colunasClientes = {"Nome", "Sexo", "Email", "Telefone", "CPF", "CEP"};
        DefaultTableModel modeloTabela = new DefaultTableModel(colunasClientes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Cliente cliente : clientes) {
            modeloTabela.addRow(new Object[]{cliente.nome, cliente.sexo, cliente.email, cliente.telefone, cliente.cpf, cliente.cep});
        }
        JTable tabelaClientes = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaClientes);

        tabelaClientes.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelaClientes.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabelaClientes.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaClientes.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabelaClientes.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabelaClientes.getColumnModel().getColumn(5).setPreferredWidth(80);

        JPanel botoesPanel = new JPanel(new FlowLayout());
        JButton btnEditar = new JButton("Editar");
        JButton btnApagar = new JButton("Apagar");

        botoesPanel.add(btnEditar);
        botoesPanel.add(btnApagar);

        JPanel buscaPanel = new JPanel(new FlowLayout());
        buscaPanel.add(new JLabel("Buscar:"));
        buscaPanel.add(txtBusca);

        frame.add(buscaPanel, BorderLayout.NORTH);
        frame.add(scrollTabela, BorderLayout.CENTER);
        frame.add(botoesPanel, BorderLayout.SOUTH);

        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrarClientes(); }
            public void removeUpdate(DocumentEvent e) { filtrarClientes(); }
            public void changedUpdate(DocumentEvent e) { filtrarClientes(); }

            private void filtrarClientes() {
                String busca = txtBusca.getText().trim().toLowerCase();
                modeloTabela.setRowCount(0);
                for (Cliente cliente : clientes) {
                    if (cliente.nome.toLowerCase().contains(busca) ||
                        cliente.email.toLowerCase().contains(busca) ||
                        cliente.telefone.contains(busca) ||
                        cliente.cpf.contains(busca) ||
                        cliente.cep.contains(busca) ||
                        cliente.sexo.toLowerCase().contains(busca)) {
                        modeloTabela.addRow(new Object[]{cliente.nome, cliente.sexo, cliente.email, cliente.telefone, cliente.cpf, cliente.cep});
                    }
                }
            }
        });

        btnEditar.addActionListener(e -> {
            int row = tabelaClientes.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Selecione um cliente!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String nomeSelecionado = String.valueOf(modeloTabela.getValueAt(row, 0));
            Cliente cliente = clientes.stream().filter(c -> c.nome.equals(nomeSelecionado)).findFirst().orElse(null);
            if (cliente == null) return;

            int clienteIndex = clientes.indexOf(cliente);
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 10, 6, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;

            JTextField txtNome = new JTextField(cliente.nome, 20);
            JTextField txtEmail = new JTextField(cliente.email, 20);
            JTextField txtTelefone = new JTextField(cliente.telefone, 20);
            JTextField txtCPF = new JTextField(cliente.cpf, 20);
            JTextField txtCEP = new JTextField(cliente.cep, 20);
            JComboBox<String> comboSexo = new JComboBox<>(new String[]{"Masculino", "Feminino", "Não Binário", "Prefiro não responder", "Outros"});
            comboSexo.setSelectedItem(cliente.sexo.equals("Masculino") || cliente.sexo.equals("Feminino") || 
                                      cliente.sexo.equals("Não Binário") || cliente.sexo.equals("Prefiro não responder") ? cliente.sexo : "Outros");
            JTextField txtSexoOutros = new JTextField(cliente.sexo.equals("Masculino") || cliente.sexo.equals("Feminino") || 
                                                     cliente.sexo.equals("Não Binário") || cliente.sexo.equals("Prefiro não responder") ? "" : cliente.sexo, 20);
            boolean isOutros = comboSexo.getSelectedItem().equals("Outros");
            txtSexoOutros.setVisible(isOutros);
            txtSexoOutros.setEnabled(isOutros);

            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("Nome do Cliente:"), gbc);
            gbc.gridx = 1;
            panel.add(txtNome, gbc);
            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Email:"), gbc);
            gbc.gridx = 1;
            panel.add(txtEmail, gbc);
            gbc.gridx = 0; gbc.gridy = 2;
            panel.add(new JLabel("Telefone (Com DDD):"), gbc);
            gbc.gridx = 1;
            panel.add(txtTelefone, gbc);
            gbc.gridx = 0; gbc.gridy = 3;
            panel.add(new JLabel("CPF:"), gbc);
            gbc.gridx = 1;
            panel.add(txtCPF, gbc);
            gbc.gridx = 0; gbc.gridy = 4;
            panel.add(new JLabel("CEP:"), gbc);
            gbc.gridx = 1;
            panel.add(txtCEP, gbc);
            gbc.gridx = 0; gbc.gridy = 5;
            panel.add(new JLabel("Sexo:"), gbc);
            gbc.gridx = 1;
            panel.add(comboSexo, gbc);
            gbc.gridx = 1; gbc.gridy = 6;
            panel.add(txtSexoOutros, gbc);

            comboSexo.addActionListener(ev -> {
                boolean outrosSelecionado = comboSexo.getSelectedItem().equals("Outros");
                txtSexoOutros.setVisible(outrosSelecionado);
                txtSexoOutros.setEnabled(outrosSelecionado);
                if (!outrosSelecionado) {
                    txtSexoOutros.setText("");
                }
                panel.revalidate();
                panel.repaint();
            });

            int opcao = JOptionPane.showConfirmDialog(frame, panel, "Editar Cliente", JOptionPane.OK_CANCEL_OPTION);
            if (opcao == JOptionPane.OK_OPTION) {
                String novoNome = txtNome.getText().trim();
                String novoEmail = txtEmail.getText().trim();
                String novoTelefone = txtTelefone.getText().trim();
                String novoCPF = txtCPF.getText().trim();
                String novoCEP = txtCEP.getText().trim();
                String novoSexo = comboSexo.getSelectedItem().equals("Outros") ? txtSexoOutros.getText().trim() : (String) comboSexo.getSelectedItem();

                if (novoNome.length() >= 3 && Pattern.matches("[\\p{L}\\s]+", novoNome)) {
                    if (!novoEmail.isEmpty() && !Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", novoEmail)) {
                        JOptionPane.showMessageDialog(frame, "Email inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!novoTelefone.isEmpty() && !Pattern.matches("\\d{11}", novoTelefone)) {
                        JOptionPane.showMessageDialog(frame, "Telefone deve ter 11 dígitos numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!novoCPF.isEmpty()) {
                        if (!Pattern.matches("\\d{11}", novoCPF)) {
                            JOptionPane.showMessageDialog(frame, "CPF deve ter 11 dígitos numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (clientes.stream().anyMatch(c -> !c.equals(cliente) && c.cpf.equalsIgnoreCase(novoCPF))) {
                            JOptionPane.showMessageDialog(frame, "CPF já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    if (!novoCEP.isEmpty() && !Pattern.matches("\\d{8}", novoCEP)) {
                        JOptionPane.showMessageDialog(frame, "CEP deve ter 8 dígitos numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (comboSexo.getSelectedItem().equals("Outros") && 
                        (novoSexo.isEmpty() || !Pattern.matches("[\\p{L}\\s]+", novoSexo) || novoSexo.length() < 2)) {
                        JOptionPane.showMessageDialog(frame, "Sexo inválido para 'Outros'!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (clientes.stream().anyMatch(c -> !c.equals(cliente) && c.nome.equalsIgnoreCase(novoNome))) {
                        JOptionPane.showMessageDialog(frame, "Nome já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    cliente.nome = novoNome;
                    cliente.email = novoEmail;
                    cliente.telefone = novoTelefone;
                    cliente.cpf = novoCPF;
                    cliente.cep = novoCEP;
                    cliente.sexo = novoSexo;
                    modeloTabela.setValueAt(novoNome, row, 0);
                    modeloTabela.setValueAt(novoSexo, row, 1);
                    modeloTabela.setValueAt(novoEmail, row, 2);
                    modeloTabela.setValueAt(novoTelefone, row, 3);
                    modeloTabela.setValueAt(novoCPF, row, 4);
                    modeloTabela.setValueAt(novoCEP, row, 5);
                    salvarDados();
                } else {
                    JOptionPane.showMessageDialog(frame, "Nome inválido (mín. 3 letras, apenas letras e espaços)!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnApagar.addActionListener(e -> {
            int row = tabelaClientes.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Selecione um cliente!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String nomeCliente = String.valueOf(modeloTabela.getValueAt(row, 0));
            Cliente cliente = clientes.stream().filter(c -> c.nome.equals(nomeCliente)).findFirst().orElse(null);
            if (cliente == null) return;

            int opcao = JOptionPane.showConfirmDialog(frame, "Deseja apagar o cliente " + cliente.nome + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                clientes.remove(cliente);
                modeloTabela.removeRow(row);
                for (Animal animal : animais) {
                    animal.removerCliente(cliente);
                }
                salvarDados();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void abrirTelaAnimais() {
    JFrame frame = new JFrame("Gerenciar Animais");
    frame.setSize(700, 400);
    frame.setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JTextField txtBusca = new JTextField(20);
    JCheckBox chkFiltroAdocao = new JCheckBox("Mostrar apenas para adoção");
    DefaultTableModel modeloTabela = new DefaultTableModel(new Object[]{"Nome", "Donos", "Observação", "Raça", "Sexo"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    for (Animal animal : animais) {
        modeloTabela.addRow(new Object[]{animal.nome, animal.getDonos(), animal.observacao, animal.raca, animal.sexo});
    }
    JTable tabelaAnimais = new JTable(modeloTabela);
    JScrollPane scrollTabela = new JScrollPane(tabelaAnimais);

    tabelaAnimais.getColumnModel().getColumn(0).setPreferredWidth(150);
    tabelaAnimais.getColumnModel().getColumn(1).setPreferredWidth(200);
    tabelaAnimais.getColumnModel().getColumn(2).setPreferredWidth(150);
    tabelaAnimais.getColumnModel().getColumn(3).setPreferredWidth(100);
    tabelaAnimais.getColumnModel().getColumn(4).setPreferredWidth(80);

    JPanel botoesPanel = new JPanel(new FlowLayout());
    JButton btnEditar = new JButton("Editar");
    JButton btnApagar = new JButton("Apagar");

    botoesPanel.add(btnEditar);
    botoesPanel.add(btnApagar);

    JPanel buscaPanel = new JPanel(new FlowLayout());
    buscaPanel.add(new JLabel("Buscar:"));
    buscaPanel.add(txtBusca);
    buscaPanel.add(chkFiltroAdocao);

    frame.add(buscaPanel, BorderLayout.NORTH);
    frame.add(scrollTabela, BorderLayout.CENTER);
    frame.add(botoesPanel, BorderLayout.SOUTH);

    Runnable atualizarTabela = () -> {
        String busca = txtBusca.getText().trim().toLowerCase();
        boolean filtroAdocao = chkFiltroAdocao.isSelected();
        modeloTabela.setRowCount(0);
        for (Animal animal : animais) {
            if ((animal.nome.toLowerCase().contains(busca) || 
                 animal.getDonos().toLowerCase().contains(busca) || 
                 animal.raca.toLowerCase().contains(busca) ||
                 animal.sexo.toLowerCase().contains(busca)) &&
                (!filtroAdocao || animal.paraAdocao)) {
                modeloTabela.addRow(new Object[]{animal.nome, animal.getDonos(), animal.observacao, animal.raca, animal.sexo});
            }
        }
    };

    txtBusca.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { atualizarTabela.run(); }
        public void removeUpdate(DocumentEvent e) { atualizarTabela.run(); }
        public void changedUpdate(DocumentEvent e) { atualizarTabela.run(); }
    });

    chkFiltroAdocao.addActionListener(e -> atualizarTabela.run());

    btnEditar.addActionListener(e -> {
        int row = tabelaAnimais.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um animal!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nomeAnimal = (String) modeloTabela.getValueAt(row, 0);
        Animal animal = animais.stream().filter(a -> a.nome.equals(nomeAnimal)).findFirst().orElse(null);
        if (animal == null) return;

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField txtNome = new JTextField(animal.nome, 20);
        JCheckBox chkSemDono = new JCheckBox("Sem dono (Para adoção)", animal.semDono);
        JComboBox<String> comboRaca = new JComboBox<>(new String[]{"Cachorro", "Gato", "Roedor", "Aves", "Outros"});
        comboRaca.setSelectedItem(animal.raca.equals("Cachorro") || animal.raca.equals("Gato") || 
                                  animal.raca.equals("Roedor") || animal.raca.equals("Aves") ? animal.raca : "Outros");
        JTextField txtRacaOutros = new JTextField(animal.raca.equals("Cachorro") || animal.raca.equals("Gato") || 
                                                 animal.raca.equals("Roedor") || animal.raca.equals("Aves") ? "" : animal.raca, 20);
        boolean isOutrosRaca = comboRaca.getSelectedItem().equals("Outros");
        txtRacaOutros.setVisible(isOutrosRaca);
        txtRacaOutros.setEnabled(isOutrosRaca);
        JComboBox<String> comboSexoAnimal = new JComboBox<>(new String[]{"Macho", "Fêmea"});
        comboSexoAnimal.setSelectedItem(animal.sexo);
        JComboBox<String> comboClientes = new JComboBox<>();
        comboClientes.addItem("Selecione um cliente");
        for (Cliente cliente : clientes) {
            if (!animal.clientes.contains(cliente)) {
                comboClientes.addItem(cliente.toString());
            }
        }
        JTextField txtNovoCliente = new JTextField(20);
        JButton btnAdicionarCliente = new JButton("Adicionar");
        DefaultListModel<Cliente> modeloClientes = new DefaultListModel<>();
        for (Cliente cliente : animal.clientes) {
            modeloClientes.addElement(cliente);
        }
        JList<Cliente> listaClientes = new JList<>(modeloClientes);
        JScrollPane scrollClientes = new JScrollPane(listaClientes);
        JButton btnRemoverCliente = new JButton("Remover");
        JTextArea txtObservacao = new JTextArea(animal.observacao, 5, 20);
        JScrollPane scrollObservacao = new JScrollPane(txtObservacao);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome do Animal:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(txtNome, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
        panel.add(chkSemDono, gbc);
        gbc.gridy = 2;
        panel.add(new JLabel("Raça:"), gbc);
        gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(comboRaca, gbc);
        gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(txtRacaOutros, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        panel.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(comboSexoAnimal, gbc);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3;
        panel.add(new JLabel("Clientes Associados:"), gbc);
        gbc.gridy = 7; gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollClientes, gbc);
        gbc.gridy = 8; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnRemoverCliente, gbc);
        gbc.gridy = 9;
        panel.add(new JLabel("Associar Cliente Existente:"), gbc);
        gbc.gridy = 10;
        panel.add(comboClientes, gbc);
        gbc.gridy = 11;
        panel.add(btnAdicionarCliente, gbc);
        gbc.gridy = 12;
        panel.add(new JLabel("Novo Cliente:"), gbc);
        gbc.gridy = 13;
        panel.add(txtNovoCliente, gbc);
        gbc.gridy = 14;
        panel.add(new JLabel("Observação:"), gbc);
        gbc.gridy = 15;
        panel.add(scrollObservacao, gbc);

        comboRaca.addActionListener(ev -> {
            boolean outrosSelecionado = comboRaca.getSelectedItem().equals("Outros");
            txtRacaOutros.setVisible(outrosSelecionado);
            txtRacaOutros.setEnabled(outrosSelecionado);
            if (!outrosSelecionado) {
                txtRacaOutros.setText("");
            }
            panel.revalidate();
            panel.repaint();
        });

        btnAdicionarCliente.addActionListener(ev -> {
            String clienteNome = (String) comboClientes.getSelectedItem();
            String novoClienteNome = txtNovoCliente.getText().trim();
            Cliente cliente = null;
            if (clienteNome != null && !clienteNome.equals("Selecione um cliente")) {
                cliente = clientes.stream().filter(c -> c.toString().equals(clienteNome)).findFirst().orElse(null);
            } else if (!novoClienteNome.isEmpty() && novoClienteNome.length() >= 3 && Pattern.matches("[\\p{L}\\s]+", novoClienteNome)) {
                if (!clientes.stream().anyMatch(c -> c.nome.equalsIgnoreCase(novoClienteNome))) {
                    cliente = new Cliente(novoClienteNome, "", "", "", "", "Prefiro não responder");
                    clientes.add(cliente);
                    salvarDados();
                }
            }
            if (cliente != null && !modeloClientes.contains(cliente)) {
                modeloClientes.addElement(cliente);
                comboClientes.removeItem(clienteNome);
                chkSemDono.setSelected(false);
                txtNovoCliente.setText("");
            }
        });

        btnRemoverCliente.addActionListener(ev -> {
            Cliente clienteSelecionado = listaClientes.getSelectedValue();
            if (clienteSelecionado != null) {
                modeloClientes.removeElement(clienteSelecionado);
                if (modeloClientes.isEmpty()) {
                    chkSemDono.setSelected(true);
                }
            }
        });

        int opcao = JOptionPane.showConfirmDialog(frame, panel, "Editar Animal", JOptionPane.OK_CANCEL_OPTION);
        if (opcao == JOptionPane.OK_OPTION) {
            String novoNome = txtNome.getText().trim();
            boolean semDono = chkSemDono.isSelected();
            String novaObservacao = txtObservacao.getText();
            String novaRaca = comboRaca.getSelectedItem().equals("Outros") ? txtRacaOutros.getText().trim() : (String) comboRaca.getSelectedItem();
            String novoSexo = (String) comboSexoAnimal.getSelectedItem();
            if (novoNome.length() >= 2 && Pattern.matches("[\\p{L}\\s]+", novoNome)) {
                if (semDono && !modeloClientes.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Animais com clientes não podem ser sem dono!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (comboRaca.getSelectedItem().equals("Outros") && 
                    (novaRaca.isEmpty() || !Pattern.matches("[\\p{L}\\s]+", novaRaca) || novaRaca.length() < 2)) {
                    JOptionPane.showMessageDialog(frame, "Raça inválida para 'Outros' (mín. 2 letras, apenas letras e espaços)!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                animal.nome = novoNome;
                animal.semDono = semDono;
                animal.paraAdocao = semDono;
                animal.observacao = novaObservacao;
                animal.raca = novaRaca;
                animal.sexo = novoSexo;
                animal.clientes.clear();
                for (int i = 0; i < modeloClientes.size(); i++) {
                    animal.adicionarCliente(modeloClientes.get(i));
                }
                modeloTabela.setValueAt(animal.nome, row, 0);
                modeloTabela.setValueAt(animal.getDonos(), row, 1);
                modeloTabela.setValueAt(animal.observacao, row, 2);
                modeloTabela.setValueAt(animal.raca, row, 3);
                modeloTabela.setValueAt(animal.sexo, row, 4);
                salvarDados();
            } else {
                JOptionPane.showMessageDialog(frame, "Nome inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    btnApagar.addActionListener(e -> {
        int row = tabelaAnimais.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um animal!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nomeAnimal = (String) modeloTabela.getValueAt(row, 0);
        Animal animal = animais.stream().filter(a -> a.nome.equals(nomeAnimal)).findFirst().orElse(null);
        if (animal == null) return;

        int opcao = JOptionPane.showConfirmDialog(frame, "Deseja apagar o animal " + animal.nome + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            animais.remove(animal);
            modeloTabela.removeRow(row);
            salvarDados();
        }
    });

    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}

    private static void abrirTelaCadastro() {
        JFrame frame = new JFrame("Cadastro de Clientes e Animais");
        frame.setSize(800, 600);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelClientes = new JPanel(new GridBagLayout());
        JScrollPane scrollClientes = new JScrollPane(panelClientes);
        scrollClientes.setPreferredSize(new Dimension(750, 200));
        JPanel panelAnimais = new JPanel(new GridBagLayout());
        JScrollPane scrollAnimais = new JScrollPane(panelAnimais);
        scrollAnimais.setPreferredSize(new Dimension(750, 150));
        ArrayList<JTextField> camposNomesClientes = new ArrayList<>();
        ArrayList<JTextField> camposEmails = new ArrayList<>();
        ArrayList<JTextField> camposTelefones = new ArrayList<>();
        ArrayList<JTextField> camposCPFs = new ArrayList<>();
        ArrayList<JTextField> camposCEPs = new ArrayList<>();
        ArrayList<JComboBox<String>> combosSexo = new ArrayList<>();
        ArrayList<JTextField> camposSexoOutros = new ArrayList<>();
        ArrayList<JTextField> camposAnimais = new ArrayList<>();
        ArrayList<JComboBox<String>> combosRaca = new ArrayList<>();
        ArrayList<JTextField> camposRacaOutros = new ArrayList<>();
        ArrayList<JComboBox<String>> camposSexoAnimais = new ArrayList<>();
        JTextArea txtObservacao = new JTextArea(5, 40);
        JLabel lblStatus = new JLabel("");

        JComboBox<Integer> comboQtdClientes = new JComboBox<>();
        JCheckBox chkSemDonoGlobal = new JCheckBox("Sem dono (Para adoção)");
        JComboBox<Integer> comboQtdAnimais = new JComboBox<>();
        for (int i = 1; i <= 10; i++) {
            comboQtdClientes.addItem(i);
            comboQtdAnimais.addItem(i);
        }

        JButton btnCadastrar = new JButton("Cadastrar");

        GridBagConstraints gbcPanel = new GridBagConstraints();
        gbcPanel.insets = new Insets(3, 3, 3, 3);
        gbcPanel.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        frame.add(new JLabel("Quantidade de Clientes:"), gbc);
        gbc.gridx = 1;
        frame.add(comboQtdClientes, gbc);
        gbc.gridx = 2;
        frame.add(chkSemDonoGlobal, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        frame.add(new JLabel("Clientes:"), gbc);
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(scrollClientes, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 3;
        frame.add(new JLabel("Quantidade de Animais:"), gbc);
        gbc.gridx = 1;
        frame.add(comboQtdAnimais, gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        frame.add(new JLabel("Animais:"), gbc);
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(scrollAnimais, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 6;
        frame.add(new JLabel("Observação:"), gbc);
        gbc.gridy = 7;
        frame.add(new JScrollPane(txtObservacao), gbc);
        gbc.gridy = 8;
        frame.add(btnCadastrar, gbc);
        gbc.gridy = 9;
        frame.add(lblStatus, gbc);

        ActionListener clienteListener = e -> {
            if (!chkSemDonoGlobal.isSelected()) {
                int qtd = (Integer) comboQtdClientes.getSelectedItem();
                panelClientes.removeAll();
                camposNomesClientes.clear();
                camposEmails.clear();
                camposTelefones.clear();
                camposCPFs.clear();
                camposCEPs.clear();
                combosSexo.clear();
                camposSexoOutros.clear();
                gbcPanel.gridx = 0;
                gbcPanel.gridy = 0;

                for (int i = 0; i < qtd; i++) {
                    JLabel lblCliente = new JLabel("Cliente " + (i + 1) + ":");
                    gbcPanel.gridx = 0; gbcPanel.gridy++;
                    gbcPanel.gridwidth = 4;
                    panelClientes.add(lblCliente, gbcPanel);
                    gbcPanel.gridwidth = 1;

                    JTextField txtNome = new JTextField(15);
                    JTextField txtEmail = new JTextField(15);
                    gbcPanel.gridx = 0; gbcPanel.gridy++;
                    panelClientes.add(new JLabel("Nome:"), gbcPanel);
                    gbcPanel.gridx = 1;
                    panelClientes.add(txtNome, gbcPanel);
                    gbcPanel.gridx = 2;
                    panelClientes.add(new JLabel("Email:"), gbcPanel);
                    gbcPanel.gridx = 3;
                    panelClientes.add(txtEmail, gbcPanel);

                    JTextField txtTelefone = new JTextField(15);
                    JTextField txtCPF = new JTextField(15);
                    gbcPanel.gridx = 0; gbcPanel.gridy++;
                    panelClientes.add(new JLabel("Telefone:"), gbcPanel);
                    gbcPanel.gridx = 1;
                    panelClientes.add(txtTelefone, gbcPanel);
                    gbcPanel.gridx = 2;
                    panelClientes.add(new JLabel("CPF:"), gbcPanel);
                    gbcPanel.gridx = 3;
                    panelClientes.add(txtCPF, gbcPanel);

                    JTextField txtCEP = new JTextField(15);
                    JComboBox<String> comboSexo = new JComboBox<>(new String[]{"Masculino", "Feminino", "Não Binário", "Prefiro não responder", "Outros"});
                    gbcPanel.gridx = 0; gbcPanel.gridy++;
                    panelClientes.add(new JLabel("CEP:"), gbcPanel);
                    gbcPanel.gridx = 1;
                    panelClientes.add(txtCEP, gbcPanel);
                    gbcPanel.gridx = 2;
                    panelClientes.add(new JLabel("Sexo:"), gbcPanel);
                    gbcPanel.gridx = 3;
                    panelClientes.add(comboSexo, gbcPanel);

                    JTextField txtSexoOutros = new JTextField(15);
                    txtSexoOutros.setVisible(false);
                    txtSexoOutros.setEnabled(false);
                    gbcPanel.gridx = 1; gbcPanel.gridy++;
                    gbcPanel.gridwidth = 3;
                    panelClientes.add(txtSexoOutros, gbcPanel);
                    gbcPanel.gridwidth = 1;

                    camposNomesClientes.add(txtNome);
                    camposEmails.add(txtEmail);
                    camposTelefones.add(txtTelefone);
                    camposCPFs.add(txtCPF);
                    camposCEPs.add(txtCEP);
                    combosSexo.add(comboSexo);
                    camposSexoOutros.add(txtSexoOutros);

                    comboSexo.addActionListener(ev -> {
                        boolean outrosSelecionado = comboSexo.getSelectedItem().equals("Outros");
                        txtSexoOutros.setVisible(outrosSelecionado);
                        txtSexoOutros.setEnabled(outrosSelecionado);
                        if (!outrosSelecionado) {
                            txtSexoOutros.setText("");
                        }
                        panelClientes.revalidate();
                        panelClientes.repaint();
                    });
                }

                panelClientes.revalidate();
                frame.revalidate();
            }
        };

        ActionListener animalListener = e -> {
            int qtd = (Integer) comboQtdAnimais.getSelectedItem();
            panelAnimais.removeAll();
            camposAnimais.clear();
            combosRaca.clear();
            camposRacaOutros.clear();
            camposSexoAnimais.clear();
            gbcPanel.gridx = 0;
            gbcPanel.gridy = 0;

            for (int i = 0; i < qtd; i++) {
                JLabel lblAnimal = new JLabel("Animal " + (i + 1) + ":");
                gbcPanel.gridx = 0; gbcPanel.gridy++;
                gbcPanel.gridwidth = 6;
                panelAnimais.add(lblAnimal, gbcPanel);
                gbcPanel.gridwidth = 1;

                JTextField txtAnimal = new JTextField(15);
                JComboBox<String> comboRaca = new JComboBox<>(new String[]{"Cachorro", "Gato", "Roedor", "Aves", "Outros"});
                JTextField txtRacaOutros = new JTextField(15);
                JComboBox<String> comboSexoAnimal = new JComboBox<>(new String[]{"Macho", "Fêmea"});
                txtRacaOutros.setVisible(false);
                txtRacaOutros.setEnabled(false);
                camposAnimais.add(txtAnimal);
                combosRaca.add(comboRaca);
                camposRacaOutros.add(txtRacaOutros);
                camposSexoAnimais.add(comboSexoAnimal);

                gbcPanel.gridx = 0; gbcPanel.gridy++;
                panelAnimais.add(new JLabel("Nome:"), gbcPanel);
                gbcPanel.gridx = 1;
                panelAnimais.add(txtAnimal, gbcPanel);
                gbcPanel.gridx = 2;
                panelAnimais.add(new JLabel("Raça:"), gbcPanel);
                gbcPanel.gridx = 3;
                panelAnimais.add(comboRaca, gbcPanel);
                gbcPanel.gridx = 4;
                panelAnimais.add(new JLabel("Sexo:"), gbcPanel);
                gbcPanel.gridx = 5;
                panelAnimais.add(comboSexoAnimal, gbcPanel);
                gbcPanel.gridx = 3; gbcPanel.gridy++;
                gbcPanel.gridwidth = 3;
                panelAnimais.add(txtRacaOutros, gbcPanel);
                gbcPanel.gridwidth = 1;

                comboRaca.addActionListener(ev -> {
                    boolean outrosSelecionado = comboRaca.getSelectedItem().equals("Outros");
                    txtRacaOutros.setVisible(outrosSelecionado);
                    txtRacaOutros.setEnabled(outrosSelecionado);
                    if (!outrosSelecionado) {
                        txtRacaOutros.setText("");
                    }
                    panelAnimais.revalidate();
                    panelAnimais.repaint();
                });
            }

            panelAnimais.revalidate();
            frame.revalidate();
        };

        chkSemDonoGlobal.addActionListener(e -> {
            boolean semDono = chkSemDonoGlobal.isSelected();
            comboQtdClientes.setEnabled(!semDono);
            panelClientes.removeAll();
            camposNomesClientes.clear();
            camposEmails.clear();
            camposTelefones.clear();
            camposCPFs.clear();
            camposCEPs.clear();
            combosSexo.clear();
            camposSexoOutros.clear();
            if (semDono) {
                panelClientes.revalidate();
                frame.revalidate();
            } else {
                comboQtdClientes.setSelectedItem(1);
                clienteListener.actionPerformed(null);
            }
        });

        comboQtdClientes.addActionListener(clienteListener);
        comboQtdAnimais.addActionListener(animalListener);

        comboQtdClientes.setSelectedItem(1);
        comboQtdAnimais.setSelectedItem(1);
        clienteListener.actionPerformed(null);
        animalListener.actionPerformed(null);

        btnCadastrar.addActionListener(e -> {
            boolean erro = false;
            ArrayList<Cliente> novosClientes = new ArrayList<>();
            ArrayList<Animal> novosAnimais = new ArrayList<>();

            if (!chkSemDonoGlobal.isSelected()) {
                for (int i = 0; i < camposNomesClientes.size(); i++) {
                    String nome = camposNomesClientes.get(i).getText().trim();
                    String email = camposEmails.get(i).getText().trim();
                    String telefone = camposTelefones.get(i).getText().trim();
                    String cpf = camposCPFs.get(i).getText().trim();
                    String cep = camposCEPs.get(i).getText().trim();
                    String sexo = combosSexo.get(i).getSelectedItem().equals("Outros") ? 
                                  camposSexoOutros.get(i).getText().trim() : (String) combosSexo.get(i).getSelectedItem();

                    if (!nome.isEmpty()) {
                        if (nome.length() < 3 || !Pattern.matches("[\\p{L}\\s]+", nome)) {
                            lblStatus.setText("Erro: Nome de cliente inválido (mín. 3 letras, apenas letras e espaços).");
                            erro = true;
                            break;
                        }
                        if (!email.isEmpty() && !Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
                            lblStatus.setText("Erro: Email inválido.");
                            erro = true;
                            break;
                        }
                        if (!telefone.isEmpty() && !Pattern.matches("\\d{11}", telefone)) {
                            lblStatus.setText("Erro: Telefone deve ter 11 dígitos numéricos.");
                            erro = true;
                            break;
                        }
                        if (!cpf.isEmpty()) {
                            if (!Pattern.matches("\\d{11}", cpf)) {
                                lblStatus.setText("Erro: CPF deve ter 11 dígitos numéricos.");
                                erro = true;
                                break;
                            }
                            if (clientes.stream().anyMatch(c -> c.cpf.equalsIgnoreCase(cpf))) {
                                lblStatus.setText("Erro: CPF já cadastrado.");
                                erro = true;
                                break;
                            }
                        }
                        if (!cep.isEmpty() && !Pattern.matches("\\d{8}", cep)) {
                            lblStatus.setText("Erro: CEP deve ter 8 dígitos numéricos.");
                            erro = true;
                            break;
                        }
                        if (combosSexo.get(i).getSelectedItem().equals("Outros") && 
                            (sexo.isEmpty() || !Pattern.matches("[\\p{L}\\s]+", sexo) || sexo.length() < 2)) {
                            lblStatus.setText("Erro: Sexo inválido para 'Outros' (mín. 2 letras, apenas letras e espaços).");
                            erro = true;
                            break;
                        }
                        novosClientes.add(new Cliente(nome, email, telefone, cpf, cep, sexo));
                    }
                }
            }

            for (int i = 0; i < camposAnimais.size(); i++) {
                JTextField txt = camposAnimais.get(i);
                JComboBox<String> comboRaca = combosRaca.get(i);
                JTextField txtRacaOutros = camposRacaOutros.get(i);
                JComboBox<String> comboSexoAnimal = camposSexoAnimais.get(i);
                String nome = txt.getText().trim();
                String raca = comboRaca.getSelectedItem().equals("Outros") ? txtRacaOutros.getText().trim() : (String) comboRaca.getSelectedItem();
                String sexo = (String) comboSexoAnimal.getSelectedItem();
                if (!nome.isEmpty()) {
                    if (nome.length() >= 2 && Pattern.matches("[\\p{L}\\s]+", nome)) {
                        if (comboRaca.getSelectedItem().equals("Outros") && 
                            (raca.isEmpty() || !Pattern.matches("[\\p{L}\\s]+", raca) || raca.length() < 2)) {
                            lblStatus.setText("Erro: Raça inválida para 'Outros' (mín. 2 letras, apenas letras e espaços).");
                            erro = true;
                            break;
                        }
                        novosAnimais.add(new Animal(nome, chkSemDonoGlobal.isSelected(), txtObservacao.getText(), raca, sexo));
                    } else {
                        lblStatus.setText("Erro: Nome de animal inválido (mín. 2 letras, apenas letras e espaços).");
                        erro = true;
                        break;
                    }
                }
            }

            if (!erro) {
                for (Cliente cliente : novosClientes) {
                    if (!clientes.stream().anyMatch(c -> c.nome.equalsIgnoreCase(cliente.nome))) {
                        clientes.add(cliente);
                    }
                }

                for (Animal animal : novosAnimais) {
                    if (!animal.semDono) {
                        for (Cliente cliente : novosClientes) {
                            animal.adicionarCliente(cliente);
                        }
                    }
                    animais.add(animal);
                }

                camposNomesClientes.forEach(txt -> txt.setText(""));
                camposEmails.forEach(txt -> txt.setText(""));
                camposTelefones.forEach(txt -> txt.setText(""));
                camposCPFs.forEach(txt -> txt.setText(""));
                camposCEPs.forEach(txt -> txt.setText(""));
                combosSexo.forEach(combo -> combo.setSelectedItem("Prefiro não responder"));
                camposSexoOutros.forEach(txt -> txt.setText(""));
                camposAnimais.forEach(txt -> txt.setText(""));
                camposRacaOutros.forEach(txt -> txt.setText(""));
                combosRaca.forEach(combo -> combo.setSelectedItem("Cachorro"));
                camposSexoAnimais.forEach(combo -> combo.setSelectedItem("Macho"));
                txtObservacao.setText("");
                chkSemDonoGlobal.setSelected(false);
                comboQtdClientes.setEnabled(true);
                comboQtdClientes.setSelectedItem(1);
                clienteListener.actionPerformed(null);
                lblStatus.setText("Cadastro realizado com sucesso!");
                salvarDados();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void abrirTelaAgendamentos() {
        JFrame frame = new JFrame("Gerenciar Agendamentos");
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Painel de entrada
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtBusca = new JTextField(20);
        JComboBox<Animal> comboAnimal = new JComboBox<>();
        comboAnimal.addItem(null); // Item nulo para "Selecione"
        for (Animal animal : animais) {
            comboAnimal.addItem(animal);
        }
        JSpinner spinnerData = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor editorData = new JSpinner.DateEditor(spinnerData, "dd/MM/yyyy");
        spinnerData.setEditor(editorData);
        JSpinner spinnerHorario = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));
        JSpinner.DateEditor editorHorario = new JSpinner.DateEditor(spinnerHorario, "HH:mm");
        spinnerHorario.setEditor(editorHorario);
        JTextArea txtDetalhes = new JTextArea(3, 20);
        JScrollPane scrollDetalhes = new JScrollPane(txtDetalhes);
        JButton btnCadastrar = new JButton("Cadastrar");

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Buscar:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        inputPanel.add(txtBusca, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Animal:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        inputPanel.add(comboAnimal, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Data:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(spinnerData, gbc);
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Horário:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(spinnerHorario, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Detalhes:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.BOTH;
        inputPanel.add(scrollDetalhes, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 1;
        inputPanel.add(btnCadastrar, gbc);

        // Tabela de agendamentos
        String[] colunasAgendamentos = {"Animal", "Data", "Horário", "Detalhes"};
        DefaultTableModel modeloTabela = new DefaultTableModel(colunasAgendamentos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Agendamento agendamento : agendamentos) {
            modeloTabela.addRow(new Object[]{agendamento.animal.toString(), agendamento.getDataFormatada(), agendamento.getHorarioFormatado(), agendamento.detalhes});
        }
        JTable tabelaAgendamentos = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaAgendamentos);
        tabelaAgendamentos.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabelaAgendamentos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabelaAgendamentos.getColumnModel().getColumn(2).setPreferredWidth(80);
        tabelaAgendamentos.getColumnModel().getColumn(3).setPreferredWidth(300);

        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout());
        JButton btnEditar = new JButton("Editar");
        JButton btnApagar = new JButton("Apagar");
        botoesPanel.add(btnEditar);
        botoesPanel.add(btnApagar);

        // Adicionar componentes ao frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollTabela, BorderLayout.CENTER);
        frame.add(botoesPanel, BorderLayout.SOUTH);

        // Filtro de busca
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrarAgendamentos(); }
            public void removeUpdate(DocumentEvent e) { filtrarAgendamentos(); }
            public void changedUpdate(DocumentEvent e) { filtrarAgendamentos(); }

            private void filtrarAgendamentos() {
                String busca = txtBusca.getText().trim().toLowerCase();
                modeloTabela.setRowCount(0);
                for (Agendamento agendamento : agendamentos) {
                    if (agendamento.animal.toString().toLowerCase().contains(busca) ||
                        agendamento.getDataFormatada().toLowerCase().contains(busca) ||
                        agendamento.getHorarioFormatado().toLowerCase().contains(busca) ||
                        agendamento.detalhes.toLowerCase().contains(busca)) {
                        modeloTabela.addRow(new Object[]{agendamento.animal.toString(), agendamento.getDataFormatada(), agendamento.getHorarioFormatado(), agendamento.detalhes});
                    }
                }
            }
        });

        // Ação de cadastrar
        btnCadastrar.addActionListener(e -> {
            Animal animal = (Animal) comboAnimal.getSelectedItem();
            Date data = (Date) spinnerData.getValue();
            Date horario = (Date) spinnerHorario.getValue();
            String detalhes = txtDetalhes.getText().trim();

            if (animal == null) {
                JOptionPane.showMessageDialog(frame, "Selecione um animal!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (detalhes.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Informe os detalhes do agendamento!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Combinar data e horário em um único Date
            Calendar calData = Calendar.getInstance();
            calData.setTime(data);
            Calendar calHorario = Calendar.getInstance();
            calHorario.setTime(horario);
            calData.set(Calendar.HOUR_OF_DAY, calHorario.get(Calendar.HOUR_OF_DAY));
            calData.set(Calendar.MINUTE, calHorario.get(Calendar.MINUTE));
            calData.set(Calendar.SECOND, 0);
            calData.set(Calendar.MILLISECOND, 0);
            Date dataHora = calData.getTime();

            Agendamento agendamento = new Agendamento(animal, dataHora, detalhes);
            agendamentos.add(agendamento);
            modeloTabela.addRow(new Object[]{agendamento.animal.toString(), agendamento.getDataFormatada(), agendamento.getHorarioFormatado(), agendamento.detalhes});
            txtDetalhes.setText("");
            comboAnimal.setSelectedItem(null);
            spinnerData.setValue(new Date());
            spinnerHorario.setValue(new Date());
            salvarDados();
            JOptionPane.showMessageDialog(frame, "Agendamento cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        });

        // Ação de editar
        btnEditar.addActionListener(e -> {
            int row = tabelaAgendamentos.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Selecione um agendamento!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Agendamento agendamento = agendamentos.get(row);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbcEdit = new GridBagConstraints();
            gbcEdit.insets = new Insets(5, 5, 5, 5);
            gbcEdit.fill = GridBagConstraints.HORIZONTAL;

            JComboBox<Animal> comboAnimalEdit = new JComboBox<>();
            comboAnimalEdit.addItem(null);
            for (Animal animal : animais) {
                comboAnimalEdit.addItem(animal);
            }
            comboAnimalEdit.setSelectedItem(agendamento.animal);
            JSpinner spinnerDataEdit = new JSpinner(new SpinnerDateModel(agendamento.horario, null, null, Calendar.DAY_OF_MONTH));
            JSpinner.DateEditor editorDataEdit = new JSpinner.DateEditor(spinnerDataEdit, "dd/MM/yyyy");
            spinnerDataEdit.setEditor(editorDataEdit);
            JSpinner spinnerHorarioEdit = new JSpinner(new SpinnerDateModel(agendamento.horario, null, null, Calendar.MINUTE));
            JSpinner.DateEditor editorHorarioEdit = new JSpinner.DateEditor(spinnerHorarioEdit, "HH:mm");
            spinnerHorarioEdit.setEditor(editorHorarioEdit);
            JTextArea txtDetalhesEdit = new JTextArea(agendamento.detalhes, 3, 20);
            JScrollPane scrollDetalhesEdit = new JScrollPane(txtDetalhesEdit);

            gbcEdit.gridx = 0; gbcEdit.gridy = 0;
            panel.add(new JLabel("Animal:"), gbcEdit);
            gbcEdit.gridx = 1; gbcEdit.gridwidth = 3;
            panel.add(comboAnimalEdit, gbcEdit);
            gbcEdit.gridwidth = 1;
            gbcEdit.gridx = 0; gbcEdit.gridy = 1;
            panel.add(new JLabel("Data:"), gbcEdit);
            gbcEdit.gridx = 1;
            panel.add(spinnerDataEdit, gbcEdit);
            gbcEdit.gridx = 2;
            panel.add(new JLabel("Horário:"), gbcEdit);
            gbcEdit.gridx = 3;
            panel.add(spinnerHorarioEdit, gbcEdit);
            gbcEdit.gridx = 0; gbcEdit.gridy = 2;
            panel.add(new JLabel("Detalhes:"), gbcEdit);
            gbcEdit.gridx = 1; gbcEdit.gridwidth = 3; gbcEdit.fill = GridBagConstraints.BOTH;
            panel.add(scrollDetalhesEdit, gbcEdit);

            int opcao = JOptionPane.showConfirmDialog(frame, panel, "Editar Agendamento", JOptionPane.OK_CANCEL_OPTION);
            if (opcao == JOptionPane.OK_OPTION) {
                Animal novoAnimal = (Animal) comboAnimalEdit.getSelectedItem();
                Date novaData = (Date) spinnerDataEdit.getValue();
                Date novoHorario = (Date) spinnerHorarioEdit.getValue();
                String novosDetalhes = txtDetalhesEdit.getText().trim();

                if (novoAnimal == null) {
                    JOptionPane.showMessageDialog(frame, "Selecione um animal!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (novosDetalhes.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Informe os detalhes do agendamento!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Combinar nova data e horário
                Calendar calData = Calendar.getInstance();
                calData.setTime(novaData);
                Calendar calHorario = Calendar.getInstance();
                calHorario.setTime(novoHorario);
                calData.set(Calendar.HOUR_OF_DAY, calHorario.get(Calendar.HOUR_OF_DAY));
                calData.set(Calendar.MINUTE, calHorario.get(Calendar.MINUTE));
                calData.set(Calendar.SECOND, 0);
                calData.set(Calendar.MILLISECOND, 0);
                Date novaDataHora = calData.getTime();

                agendamento.animal = novoAnimal;
                agendamento.horario = novaDataHora;
                agendamento.detalhes = novosDetalhes;
                modeloTabela.setValueAt(agendamento.animal.toString(), row, 0);
                modeloTabela.setValueAt(agendamento.getDataFormatada(), row, 1);
                modeloTabela.setValueAt(agendamento.getHorarioFormatado(), row, 2);
                modeloTabela.setValueAt(agendamento.detalhes, row, 3);
                salvarDados();
                JOptionPane.showMessageDialog(frame, "Agendamento editado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Ação de apagar
        btnApagar.addActionListener(e -> {
            int row = tabelaAgendamentos.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Selecione um agendamento!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Agendamento agendamento = agendamentos.get(row);
            int opcao = JOptionPane.showConfirmDialog(frame, "Deseja apagar o agendamento para " + agendamento.animal.nome + " em " + agendamento.getDataFormatada() + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                agendamentos.remove(row);
                modeloTabela.removeRow(row);
                salvarDados();
                JOptionPane.showMessageDialog(frame, "Agendamento apagado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void abrirTelaProdutos() {
        JFrame frame = new JFrame("Gerenciar Produtos");
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField txtProduto = new JTextField(20);
        JButton btnCadastrar = new JButton("Cadastrar");
        JTextArea txtLista = new JTextArea(10, 40);
        txtLista.setEditable(false);

        frame.add(new JLabel("Nome do Produto:"));
        frame.add(txtProduto);
        frame.add(btnCadastrar);
        frame.add(new JScrollPane(txtLista));

        btnCadastrar.addActionListener(e -> {
            String produto = txtProduto.getText();
            if (!produto.isEmpty()) {
                produtos.add(produto);
                txtLista.setText("Produtos:\n" + String.join("\n", produtos));
                txtProduto.setText("");
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void abrirTelaVendas() {
        JFrame frame = new JFrame("Gerenciar Vendas");
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField txtVenda = new JTextField(20);
        JButton btnCadastrar = new JButton("Cadastrar");
        JTextArea txtLista = new JTextArea(10, 40);
        txtLista.setEditable(false);

        frame.add(new JLabel("Venda (ex: Produto, Cliente):"));
        frame.add(txtVenda);
        frame.add(btnCadastrar);
        frame.add(new JScrollPane(txtLista));

        btnCadastrar.addActionListener(e -> {
            String venda = txtVenda.getText();
            if (!venda.isEmpty()) {
                vendas.add(venda);
                txtLista.setText("Vendas:\n" + String.join("\n", vendas));
                txtVenda.setText("");
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

private static void abrirTelaFuncionarios() {
    JFrame frame = new JFrame("Gerenciar Funcionários");
    frame.setSize(700, 400);
    frame.setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JTextField txtBusca = new JTextField(20);
    String[] colunasFuncionarios = {"Nome", "Sexo", "Email", "Telefone", "CPF", "CEP"};
    DefaultTableModel modeloTabela = new DefaultTableModel(colunasFuncionarios, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    for (Funcionario funcionario : funcionarios) {
        modeloTabela.addRow(new Object[]{funcionario.nome, funcionario.sexo, funcionario.email, funcionario.telefone, funcionario.cpf, funcionario.cep});
    }
    JTable tabelaFuncionarios = new JTable(modeloTabela);
    JScrollPane scrollTabela = new JScrollPane(tabelaFuncionarios);

    tabelaFuncionarios.getColumnModel().getColumn(0).setPreferredWidth(150);
    tabelaFuncionarios.getColumnModel().getColumn(1).setPreferredWidth(100);
    tabelaFuncionarios.getColumnModel().getColumn(2).setPreferredWidth(150);
    tabelaFuncionarios.getColumnModel().getColumn(3).setPreferredWidth(100);
    tabelaFuncionarios.getColumnModel().getColumn(4).setPreferredWidth(100);
    tabelaFuncionarios.getColumnModel().getColumn(5).setPreferredWidth(80);

    JPanel botoesPanel = new JPanel(new FlowLayout());
    JButton btnCadastrar = new JButton("Cadastrar");
    JButton btnEditar = new JButton("Editar");
    JButton btnApagar = new JButton("Apagar");

    botoesPanel.add(btnCadastrar);
    botoesPanel.add(btnEditar);
    botoesPanel.add(btnApagar);

    JPanel buscaPanel = new JPanel(new FlowLayout());
    buscaPanel.add(new JLabel("Buscar:"));
    buscaPanel.add(txtBusca);

    frame.add(buscaPanel, BorderLayout.NORTH);
    frame.add(scrollTabela, BorderLayout.CENTER);
    frame.add(botoesPanel, BorderLayout.SOUTH);

    txtBusca.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { filtrarFuncionarios(); }
        public void removeUpdate(DocumentEvent e) { filtrarFuncionarios(); }
        public void changedUpdate(DocumentEvent e) { filtrarFuncionarios(); }

        private void filtrarFuncionarios() {
            String busca = txtBusca.getText().trim().toLowerCase();
            modeloTabela.setRowCount(0);
            for (Funcionario funcionario : funcionarios) {
                if (funcionario.nome.toLowerCase().contains(busca) ||
                    funcionario.email.toLowerCase().contains(busca) ||
                    funcionario.telefone.contains(busca) ||
                    funcionario.cpf.contains(busca) ||
                    funcionario.cep.contains(busca) ||
                    funcionario.sexo.toLowerCase().contains(busca)) {
                    modeloTabela.addRow(new Object[]{funcionario.nome, funcionario.sexo, funcionario.email, funcionario.telefone, funcionario.cpf, funcionario.cep});
                }
            }
        }
    });

    btnCadastrar.addActionListener(e -> {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JTextField txtNome = new JTextField(20);
        JTextField txtEmail = new JTextField(20);
        JTextField txtTelefone = new JTextField(20);
        JTextField txtCPF = new JTextField(20);
        JTextField txtCEP = new JTextField(20);
        JComboBox<String> comboSexo = new JComboBox<>(new String[]{"Masculino", "Feminino", "Não Binário", "Prefiro não responder", "Outros"});
        JTextField txtSexoOutros = new JTextField(20);
        txtSexoOutros.setVisible(false);
        txtSexoOutros.setEnabled(false);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome do Funcionário:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNome, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Telefone (Com DDD):"), gbc);
        gbc.gridx = 1;
        panel.add(txtTelefone, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCPF, gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("CEP:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCEP, gbc);
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1;
        panel.add(comboSexo, gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        panel.add(txtSexoOutros, gbc);

        comboSexo.addActionListener(ev -> {
            boolean outrosSelecionado = comboSexo.getSelectedItem().equals("Outros");
            txtSexoOutros.setVisible(outrosSelecionado);
            txtSexoOutros.setEnabled(outrosSelecionado);
            if (!outrosSelecionado) {
                txtSexoOutros.setText("");
            }
            panel.revalidate();
            panel.repaint();
        });

        int opcao = JOptionPane.showConfirmDialog(frame, panel, "Cadastrar Funcionário", JOptionPane.OK_CANCEL_OPTION);
        if (opcao == JOptionPane.OK_OPTION) {
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String telefone = txtTelefone.getText().trim();
            String cpf = txtCPF.getText().trim();
            String cep = txtCEP.getText().trim();
            String sexo = comboSexo.getSelectedItem().equals("Outros") ? txtSexoOutros.getText().trim() : (String) comboSexo.getSelectedItem();

            if (nome.length() >= 3 && Pattern.matches("[\\p{L}\\s]+", nome)) {
                if (!email.isEmpty() && !Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
                    JOptionPane.showMessageDialog(frame, "Email inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!telefone.isEmpty() && !Pattern.matches("\\d{11}", telefone)) {
                    JOptionPane.showMessageDialog(frame, "Telefone deve ter 11 dígitos numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!cpf.isEmpty()) {
                    if (!Pattern.matches("\\d{11}", cpf)) {
                        JOptionPane.showMessageDialog(frame, "CPF deve ter 11 dígitos numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (funcionarios.stream().anyMatch(f -> f.cpf.equalsIgnoreCase(cpf))) {
                        JOptionPane.showMessageDialog(frame, "CPF já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if (!cep.isEmpty() && !Pattern.matches("\\d{8}", cep)) {
                    JOptionPane.showMessageDialog(frame, "CEP deve ter 8 dígitos numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (comboSexo.getSelectedItem().equals("Outros") && 
                    (sexo.isEmpty() || !Pattern.matches("[\\p{L}\\s]+", sexo) || sexo.length() < 2)) {
                    JOptionPane.showMessageDialog(frame, "Sexo inválido para 'Outros'!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (funcionarios.stream().anyMatch(f -> f.nome.equalsIgnoreCase(nome))) {
                    JOptionPane.showMessageDialog(frame, "Nome já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Funcionario funcionario = new Funcionario(nome, email, telefone, cpf, cep, sexo);
                funcionarios.add(funcionario);
                modeloTabela.addRow(new Object[]{nome, sexo, email, telefone, cpf, cep});
                salvarDados();
                JOptionPane.showMessageDialog(frame, "Funcionário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Nome inválido (mín. 3 letras, apenas letras e espaços)!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    btnEditar.addActionListener(e -> {
        int row = tabelaFuncionarios.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um funcionário!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nomeSelecionado = String.valueOf(modeloTabela.getValueAt(row, 0));
        Funcionario funcionario = funcionarios.stream().filter(f -> f.nome.equals(nomeSelecionado)).findFirst().orElse(null);
        if (funcionario == null) return;

        int funcionarioIndex = funcionarios.indexOf(funcionario);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JTextField txtNome = new JTextField(funcionario.nome, 20);
        JTextField txtEmail = new JTextField(funcionario.email, 20);
        JTextField txtTelefone = new JTextField(funcionario.telefone, 20);
        JTextField txtCPF = new JTextField(funcionario.cpf, 20);
        JTextField txtCEP = new JTextField(funcionario.cep, 20);
        JComboBox<String> comboSexo = new JComboBox<>(new String[]{"Masculino", "Feminino", "Não Binário", "Prefiro não responder", "Outros"});
        comboSexo.setSelectedItem(funcionario.sexo.equals("Masculino") || funcionario.sexo.equals("Feminino") || 
                                  funcionario.sexo.equals("Não Binário") || funcionario.sexo.equals("Prefiro não responder") ? 
                                  funcionario.sexo : "Outros");
        JTextField txtSexoOutros = new JTextField(funcionario.sexo.equals("Masculino") || funcionario.sexo.equals("Feminino") || 
                                                 funcionario.sexo.equals("Não Binário") || funcionario.sexo.equals("Prefiro não responder") ? 
                                                 "" : funcionario.sexo, 20);
        boolean isOutros = comboSexo.getSelectedItem().equals("Outros");
        txtSexoOutros.setVisible(isOutros);
        txtSexoOutros.setEnabled(isOutros);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome do Funcionário:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNome, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Telefone (Com DDD):"), gbc);
        gbc.gridx = 1;
        panel.add(txtTelefone, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCPF, gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("CEP:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCEP, gbc);
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1;
        panel.add(comboSexo, gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        panel.add(txtSexoOutros, gbc);

        comboSexo.addActionListener(ev -> {
            boolean outrosSelecionado = comboSexo.getSelectedItem().equals("Outros");
            txtSexoOutros.setVisible(outrosSelecionado);
            txtSexoOutros.setEnabled(outrosSelecionado);
            if (!outrosSelecionado) {
                txtSexoOutros.setText("");
            }
            panel.revalidate();
            panel.repaint();
        });

        int opcao = JOptionPane.showConfirmDialog(frame, panel, "Editar Funcionário", JOptionPane.OK_CANCEL_OPTION);
        if (opcao == JOptionPane.OK_OPTION) {
            String novoNome = txtNome.getText().trim();
            String novoEmail = txtEmail.getText().trim();
            String novoTelefone = txtTelefone.getText().trim();
            String novoCPF = txtCPF.getText().trim();
            String novoCEP = txtCEP.getText().trim();
            String novoSexo = comboSexo.getSelectedItem().equals("Outros") ? txtSexoOutros.getText().trim() : (String) comboSexo.getSelectedItem();

            if (novoNome.length() >= 3 && Pattern.matches("[\\p{L}\\s]+", novoNome)) {
                if (!novoEmail.isEmpty() && !Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", novoEmail)) {
                    JOptionPane.showMessageDialog(frame, "Email inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!novoTelefone.isEmpty() && !Pattern.matches("\\d{11}", novoTelefone)) {
                    JOptionPane.showMessageDialog(frame, "Telefone deve ter 11 dígitos numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!novoCPF.isEmpty()) {
                    if (!Pattern.matches("\\d{11}", novoCPF)) {
                        JOptionPane.showMessageDialog(frame, "CPF deve ter 11 dígitos numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (funcionarios.stream().anyMatch(f -> !f.equals(funcionario) && f.cpf.equalsIgnoreCase(novoCPF))) {
                        JOptionPane.showMessageDialog(frame, "CPF já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if (!novoCEP.isEmpty() && !Pattern.matches("\\d{8}", novoCEP)) {
                    JOptionPane.showMessageDialog(frame, "CEP deve ter 8 dígitos numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (comboSexo.getSelectedItem().equals("Outros") && 
                    (novoSexo.isEmpty() || !Pattern.matches("[\\p{L}\\s]+", novoSexo) || novoSexo.length() < 2)) {
                    JOptionPane.showMessageDialog(frame, "Sexo inválido para 'Outros'!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (funcionarios.stream().anyMatch(f -> !f.equals(funcionario) && f.nome.equalsIgnoreCase(novoNome))) {
                    JOptionPane.showMessageDialog(frame, "Nome já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                funcionario.nome = novoNome;
                funcionario.email = novoEmail;
                funcionario.telefone = novoTelefone;
                funcionario.cpf = novoCPF;
                funcionario.cep = novoCEP;
                funcionario.sexo = novoSexo;
                modeloTabela.setValueAt(novoNome, row, 0);
                modeloTabela.setValueAt(novoSexo, row, 1);
                modeloTabela.setValueAt(novoEmail, row, 2);
                modeloTabela.setValueAt(novoTelefone, row, 3);
                modeloTabela.setValueAt(novoCPF, row, 4);
                modeloTabela.setValueAt(novoCEP, row, 5);
                salvarDados();
                JOptionPane.showMessageDialog(frame, "Funcionário editado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Nome inválido (mín. 3 letras, apenas letras e espaços)!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    btnApagar.addActionListener(e -> {
        int row = tabelaFuncionarios.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um funcionário!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nomeFuncionario = String.valueOf(modeloTabela.getValueAt(row, 0));
        Funcionario funcionario = funcionarios.stream().filter(f -> f.nome.equals(nomeFuncionario)).findFirst().orElse(null);
        if (funcionario == null) return;

        int opcao = JOptionPane.showConfirmDialog(frame, "Deseja apagar o funcionário " + funcionario.nome + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            funcionarios.remove(funcionario);
            modeloTabela.removeRow(row);
            salvarDados();
            JOptionPane.showMessageDialog(frame, "Funcionário apagado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    });

    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}

    private static void abrirTelaRelatorios() {
        JFrame frame = new JFrame("Relatórios");
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea txtRelatorio = new JTextArea(10, 40);
        txtRelatorio.setEditable(false);
        JButton btnGerar = new JButton("Gerar Relatório");

        frame.add(btnGerar);
        frame.add(new JScrollPane(txtRelatorio));

        btnGerar.addActionListener(e -> {
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("Relatório do Sistema Petshop\n\n");
            relatorio.append("Total de Clientes: ").append(clientes.size()).append("\n");
            relatorio.append("Total de Animais: ").append(animais.size()).append("\n");
            relatorio.append("Animais para Adoção: ").append(animais.stream().filter(a -> a.paraAdocao).count()).append("\n");
            relatorio.append("Total de Agendamentos: ").append(agendamentos.size()).append("\n");
            relatorio.append("Total de Produtos: ").append(produtos.size()).append("\n");
            relatorio.append("Total de Vendas: ").append(vendas.size()).append("\n");
            relatorios.add("Total de Funcionários: " + funcionarios.size());
            txtRelatorio.setText(relatorio.toString());
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
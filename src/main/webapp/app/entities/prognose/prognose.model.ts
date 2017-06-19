import { Base } from '../base';
import { ModeloExclusivo } from '../modelo-exclusivo';
export class Prognose {
    constructor(
        public id?: number,
        public nome?: string,
        public caminho?: string,
        public mapeamento?: string,
        public salvar?: string,
        public graficos?: string,
        public estatisticas?: string,
        public forcepredict?: boolean,
        public dividir?: string,
        public agrupar?: string,
        public treino?: number,
        public fncalculavolume?: string,
        public status?: number,
        public ajuste?: Base,
        public validacao?: Base,
        public modeloExclusivo?: ModeloExclusivo,
    ) {
        this.forcepredict = false;
    }
}

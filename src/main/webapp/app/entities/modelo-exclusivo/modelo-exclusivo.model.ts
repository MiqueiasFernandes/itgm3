import { Modelo } from '../modelo';
import { Cenario } from '../cenario';
export class ModeloExclusivo {
    constructor(
        public id?: number,
        public nome?: string,
        public mapeamento?: string,
        public palpite?: string,
        public modelo?: Modelo,
        public cenario?: Cenario,
    ) {
    }
}

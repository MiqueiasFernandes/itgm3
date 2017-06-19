import { Component, OnInit } from '@angular/core';

import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Response} from '@angular/http';
import {AlertService} from 'ng-jhipster';
import {Cenario} from '../cenario.model';
import {CenarioService} from '../cenario.service';
import {Customize, CustomizeService } from '../../customize/';

@Component({
  selector: 'jhi-fab-add-cenario',
  templateUrl: './fab-add-cenario.component.html',
  styles: []
})
export class FabAddCenarioComponent implements OnInit {

    cenario: Cenario;

    constructor(
        public activeModal: NgbActiveModal,
        private customizeService: CustomizeService,
        private alertService: AlertService,
        private cenarioService: CenarioService,
    ) {
        this.cenario = new Cenario;
    }

  ngOnInit() {
  }

    criarCenario() {
        this.customizeService.getCustomize()
            .subscribe((customize: Customize) => {
                if (customize && customize.projeto) {
                    this.cenario.projeto = customize.projeto;
                    this.cenarioService.create(this.cenario)
                        .subscribe(
                            (res: Cenario) =>
                                this.onSaveSuccess(res),
                            (res: Response) => this.onError(res.json()));
                }
            });
    }

    onSaveSuccess(cenario: Cenario) {
        this.customizeService.customizeCenario(cenario);
        this.close();
    }

    private onError(error) {
        this.alertService.error(error.message);
    }

    private close() {
        this.activeModal.dismiss('closed');
    }

}

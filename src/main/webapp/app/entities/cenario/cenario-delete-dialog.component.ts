import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Cenario } from './cenario.model';
import { CenarioPopupService } from './cenario-popup.service';
import { CenarioService } from './cenario.service';

@Component({
    selector: 'jhi-cenario-delete-dialog',
    templateUrl: './cenario-delete-dialog.component.html'
})
export class CenarioDeleteDialogComponent {

    cenario: Cenario;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private cenarioService: CenarioService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['cenario']);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cenarioService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cenarioListModification',
                content: 'Deleted an cenario'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cenario-delete-popup',
    template: ''
})
export class CenarioDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cenarioPopupService: CenarioPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.cenarioPopupService
                .open(CenarioDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

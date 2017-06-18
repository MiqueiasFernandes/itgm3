import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Projeto } from './projeto.model';
import { ProjetoPopupService } from './projeto-popup.service';
import { ProjetoService } from './projeto.service';

@Component({
    selector: 'jhi-projeto-delete-dialog',
    templateUrl: './projeto-delete-dialog.component.html'
})
export class ProjetoDeleteDialogComponent {

    projeto: Projeto;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private projetoService: ProjetoService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['projeto']);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projetoService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'projetoListModification',
                content: 'Deleted an projeto'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-projeto-delete-popup',
    template: ''
})
export class ProjetoDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projetoPopupService: ProjetoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.projetoPopupService
                .open(ProjetoDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

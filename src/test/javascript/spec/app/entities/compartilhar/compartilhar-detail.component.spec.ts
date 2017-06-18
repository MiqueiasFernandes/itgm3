import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { ItgmTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CompartilharDetailComponent } from '../../../../../../main/webapp/app/entities/compartilhar/compartilhar-detail.component';
import { CompartilharService } from '../../../../../../main/webapp/app/entities/compartilhar/compartilhar.service';
import { Compartilhar } from '../../../../../../main/webapp/app/entities/compartilhar/compartilhar.model';

describe('Component Tests', () => {

    describe('Compartilhar Management Detail Component', () => {
        let comp: CompartilharDetailComponent;
        let fixture: ComponentFixture<CompartilharDetailComponent>;
        let service: CompartilharService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ItgmTestModule],
                declarations: [CompartilharDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CompartilharService,
                    EventManager
                ]
            }).overrideComponent(CompartilharDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CompartilharDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompartilharService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Compartilhar(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.compartilhar).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
